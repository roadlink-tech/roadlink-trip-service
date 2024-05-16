package com.roadlink.tripservice.usecases.driver_trip

import com.roadlink.tripservice.config.StubTimeProvider
import com.roadlink.tripservice.domain.driver_trip.DriverSectionDetail
import com.roadlink.tripservice.domain.driver_trip.Passenger
import com.roadlink.tripservice.domain.driver_trip.PassengerNotExists
import com.roadlink.tripservice.domain.driver_trip.SeatsAvailabilityStatus.*
import com.roadlink.tripservice.domain.trip.Trip
import com.roadlink.tripservice.domain.trip.TripRepository
import com.roadlink.tripservice.domain.trip.section.Section
import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.domain.trip_solicitude.TripLegSolicitudeRepository
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude.TripLegSolicitude
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude.TripLegSolicitude.Status.ACCEPTED
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude.TripLegSolicitude.Status.PENDING_APPROVAL
import com.roadlink.tripservice.domain.user.User
import com.roadlink.tripservice.domain.user.UserRepository
import com.roadlink.tripservice.domain.user.UserTrustScore
import com.roadlink.tripservice.domain.user.UserTrustScoreRepository
import com.roadlink.tripservice.usecases.common.InstantFactory.october15_12hs
import com.roadlink.tripservice.usecases.common.InstantFactory.october15_13hs
import com.roadlink.tripservice.usecases.common.InstantFactory.october15_15hs
import com.roadlink.tripservice.usecases.common.InstantFactory.october15_18hs
import com.roadlink.tripservice.usecases.common.InstantFactory.october15_7hs
import com.roadlink.tripservice.usecases.common.trip_point.TripPointFactory
import com.roadlink.tripservice.usecases.trip.SectionFactory
import com.roadlink.tripservice.usecases.trip.TripFactory
import com.roadlink.tripservice.usecases.trip.builder
import com.roadlink.tripservice.usecases.trip_solicitude.TripLegSolicitudeFactory
import com.roadlink.tripservice.usecases.user.UserFactory
import com.roadlink.tripservice.usecases.user.UserTrustScoreFactory
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class RetrieveDriverTripDetailTest {

    @MockK
    private lateinit var sectionRepository: SectionRepository

    @MockK
    private lateinit var tripLegSolicitudeRepository: TripLegSolicitudeRepository

    @MockK
    private lateinit var userRepository: UserRepository

    @MockK
    private lateinit var userTrustScoreRepository: UserTrustScoreRepository

    @MockK
    private lateinit var tripRepository: TripRepository

    private lateinit var stubTimeProvider: StubTimeProvider

    private lateinit var retrieveDriverTripDetail: RetrieveDriverTripDetail

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        stubTimeProvider = StubTimeProvider(fixedNow = october15_13hs())
        retrieveDriverTripDetail = RetrieveDriverTripDetail(
            sectionRepository = sectionRepository,
            tripRepository = tripRepository,
            tripLegSolicitudeRepository = tripLegSolicitudeRepository,
            userRepository = userRepository,
            userTrustScoreRepository = userTrustScoreRepository,
        )
    }

    @Test
    fun `trip not started`() {
        // given
        val tripId = UUID.randomUUID()
        val trip = TripFactory.common(id = tripId)
        val passengerId = UUID.randomUUID().toString()
        val section = SectionFactory.avCabildo(
            tripId = tripId,
            departure = TripPointFactory.avCabildo_4853(at = october15_15hs()),
            arrival = TripPointFactory.avCabildo_20(at = october15_18hs()),
        )
        val tripLegSolicitude = TripLegSolicitudeFactory.withSections(
            sections = listOf(section),
            passengerId = passengerId
        )

        givenSectionsAssociatedToATrip(tripId, listOf(section))
        givenTripLegSolicitudeAssociatedToASection(section.id, listOf(tripLegSolicitude))
        givenTripLegSolicitudesPending(tripId, listOf(tripLegSolicitude))
        givenUserTrustScoreWithIds(listOf(passengerId), UserTrustScoreFactory.common())
        givenAUserWithId(
            passengerId, UserFactory.common(
                id = UUID.fromString(passengerId)
            )
        )
        every { tripRepository.find(commandQuery = TripRepository.CommandQuery(ids = listOf(UUID.fromString(trip.id)))) } returns listOf(
            trip
        )

        // then
        val driverTripDetail = retrieveDriverTripDetail(
            RetrieveDriverTripDetail.Input(
                tripId = section.tripId,
            )
        )

        // then
        assertEquals(Trip.Status.NOT_STARTED, driverTripDetail.tripStatus)
    }

    @Test
    fun `trip finished`() {
        // given
        val tripId = UUID.randomUUID()
        val trip = TripFactory.common(id = tripId, status = Trip.Status.FINISHED)
        val passengerId = UUID.randomUUID().toString()
        val section = SectionFactory.avCabildo(
            tripId = tripId,
            departure = TripPointFactory.avCabildo_4853(at = october15_7hs()),
            arrival = TripPointFactory.avCabildo_20(at = october15_12hs()),
        )
        val tripLegSolicitude = TripLegSolicitudeFactory.withSections(
            sections = listOf(section),
            passengerId = passengerId,
            status = ACCEPTED
        )

        givenSectionsAssociatedToATrip(tripId, listOf(section))
        givenTripLegSolicitudeAssociatedToASection(section.id, listOf(tripLegSolicitude))
        givenTripLegSolicitudesPending(tripId)
        givenUserTrustScoreWithIds(listOf(passengerId))
        givenAUserWithId(passengerId, UserFactory.common(id = UUID.fromString(passengerId)))

        every { tripRepository.find(commandQuery = TripRepository.CommandQuery(ids = listOf(UUID.fromString(trip.id)))) } returns listOf(
            trip
        )
        // when
        val driverTripDetail = retrieveDriverTripDetail(
            RetrieveDriverTripDetail.Input(
                tripId = section.tripId,
            )
        )

        // then
        assertEquals(Trip.Status.FINISHED, driverTripDetail.tripStatus)
    }

    @Test
    fun `given a section that belongs to a trip, when get the driver trip detail then its status must be the expected`() {
        // given
        val tripId = UUID.randomUUID()
        val trip = TripFactory.common(id = tripId, status = Trip.Status.IN_PROGRESS)
        val passengerId = UUID.randomUUID().toString()
        val section = SectionFactory.avCabildo(
            tripId = tripId,
            departure = TripPointFactory.avCabildo_4853(at = october15_12hs()),
            arrival = TripPointFactory.avCabildo_20(at = october15_15hs()),
        )
        val tripLegSolicitude = TripLegSolicitudeFactory.withSections(
            sections = listOf(section),
            passengerId = passengerId
        )

        givenSectionsAssociatedToATrip(tripId, listOf(section))
        givenTripLegSolicitudeAssociatedToASection(section.id, listOf(tripLegSolicitude))
        givenTripLegSolicitudesPending(tripId, listOf(tripLegSolicitude))
        givenUserTrustScoreWithIds(listOf(passengerId))
        givenAUserWithId(passengerId, UserFactory.common(id = UUID.fromString(passengerId)))
        every { tripRepository.find(commandQuery = TripRepository.CommandQuery(ids = listOf(UUID.fromString(trip.id)))) } returns listOf(
            trip
        )

        // when
        val driverTripDetail = retrieveDriverTripDetail(
            RetrieveDriverTripDetail.Input(
                tripId = section.tripId,
            )
        )

        // then
        assertEquals(Trip.Status.IN_PROGRESS, driverTripDetail.tripStatus)
    }

    @Test
    fun `no seats available`() {
        // given
        val tripId = UUID.randomUUID()
        val trip = TripFactory.common(id = tripId, status = Trip.Status.IN_PROGRESS)
        val passengerId = UUID.randomUUID().toString()
        val section = SectionFactory.avCabildo(tripId = tripId).builder()
            .noSeatsAvailable()
            .build()
        val tripLegSolicitude = TripLegSolicitudeFactory.withSections(
            sections = listOf(section),
            passengerId = passengerId
        )

        givenSectionsAssociatedToATrip(tripId, listOf(section))
        givenTripLegSolicitudeAssociatedToASection(section.id, listOf(tripLegSolicitude))
        givenTripLegSolicitudesPending(tripId, listOf(tripLegSolicitude))
        givenUserTrustScoreWithIds(listOf(passengerId))
        givenAUserWithId(passengerId, UserFactory.common(id = UUID.fromString(passengerId)))
        every { tripRepository.find(commandQuery = TripRepository.CommandQuery(ids = listOf(UUID.fromString(trip.id)))) } returns listOf(
            trip
        )

        // when
        val driverTripDetail = retrieveDriverTripDetail(
            RetrieveDriverTripDetail.Input(
                tripId = section.tripId,
            )
        )

        // then
        assertEquals(NO_SEATS_AVAILABLE, driverTripDetail.seatStatus)
    }

    @Test
    fun `all seats available`() {
        // given
        val tripId = UUID.randomUUID()
        val trip = TripFactory.common(id = tripId, status = Trip.Status.IN_PROGRESS)
        val passengerId = UUID.randomUUID().toString()
        val section = SectionFactory.avCabildo(tripId = tripId).builder()
            .allSeatsAvailable()
            .build()
        val tripLegSolicitude = TripLegSolicitudeFactory.withSections(
            sections = listOf(section),
            passengerId = passengerId
        )

        givenSectionsAssociatedToATrip(tripId, listOf(section))
        givenTripLegSolicitudeAssociatedToASection(section.id, listOf(tripLegSolicitude))
        givenTripLegSolicitudesPending(tripId, listOf(tripLegSolicitude))
        givenUserTrustScoreWithIds(listOf(passengerId))
        givenAUserWithId(passengerId, UserFactory.common(id = UUID.fromString(passengerId)))
        every { tripRepository.find(commandQuery = TripRepository.CommandQuery(ids = listOf(UUID.fromString(trip.id)))) } returns listOf(
            trip
        )

        // when
        val driverTripDetail = retrieveDriverTripDetail(
            RetrieveDriverTripDetail.Input(
                tripId = section.tripId,
            )
        )

        // then
        assertEquals(ALL_SEATS_AVAILABLE, driverTripDetail.seatStatus)
    }

    @Test
    fun `some seats available`() {
        // given
        val tripId = UUID.randomUUID()
        val passengerId = UUID.randomUUID().toString()
        val section = SectionFactory.avCabildo(tripId = tripId).builder()
            .someSeatsAvailable()
            .build()
        val tripLegSolicitude = TripLegSolicitudeFactory.withSections(
            sections = listOf(section),
            passengerId = passengerId
        )
        val trip = TripFactory.common(id = tripId, status = Trip.Status.IN_PROGRESS)

        givenSectionsAssociatedToATrip(tripId, listOf(section))
        givenTripLegSolicitudeAssociatedToASection(section.id, listOf(tripLegSolicitude))
        givenTripLegSolicitudesPending(tripId, listOf(tripLegSolicitude))
        givenUserTrustScoreWithIds(listOf(passengerId))
        givenAUserWithId(passengerId, UserFactory.common(id = UUID.fromString(passengerId)))
        every { tripRepository.find(commandQuery = TripRepository.CommandQuery(ids = listOf(UUID.fromString(trip.id)))) } returns listOf(
            trip
        )

        // when
        val driverTripDetail = retrieveDriverTripDetail(
            RetrieveDriverTripDetail.Input(
                tripId = section.tripId
            )
        )

        // then
        assertEquals(SOME_SEATS_AVAILABLE, driverTripDetail.seatStatus)
    }

    @Test
    fun `driver section detail no passenger`() {
        // given
        val tripId = UUID.randomUUID()
        val trip = TripFactory.common(id = tripId, status = Trip.Status.IN_PROGRESS)
        val passengerId = UUID.randomUUID().toString()
        val section = SectionFactory.avCabildo(tripId = tripId)
        val tripLegSolicitude = TripLegSolicitudeFactory.withSections(
            sections = listOf(section),
            passengerId = passengerId
        )
        givenSectionsAssociatedToATrip(tripId, listOf(section))
        givenTripLegSolicitudeAssociatedToASection(section.id)
        givenTripLegSolicitudesPending(tripId, listOf(tripLegSolicitude))
        givenUserTrustScoreWithIds(listOf(passengerId))
        givenAUserWithId(passengerId, UserFactory.common(id = UUID.fromString(passengerId)))
        every { tripRepository.find(commandQuery = TripRepository.CommandQuery(ids = listOf(UUID.fromString(trip.id)))) } returns listOf(
            trip
        )

        // when
        val driverTripDetail = retrieveDriverTripDetail(
            RetrieveDriverTripDetail.Input(
                tripId = section.tripId,
            )
        )

        //  then
        assertEquals(
            listOf(
                DriverSectionDetail(
                    sectionId = SectionFactory.avCabildo_id,
                    departure = TripPointFactory.avCabildo_4853(),
                    arrival = TripPointFactory.avCabildo_20(),
                    occupiedSeats = 0,
                    availableSeats = 4,
                    passengers = emptyList(),
                )
            ),
            driverTripDetail.sectionDetails,
        )
    }

    @Test
    fun `driver section detail only consider passenger who has accepted application`() {
        // given
        val tripId = UUID.randomUUID()
        val trip = TripFactory.common(id = tripId, status = Trip.Status.IN_PROGRESS)
        val section = SectionFactory.avCabildo(
            tripId = tripId
        )
        val georgeId = UUID.randomUUID().toString()
        val georgeTripLegSolicitude = TripLegSolicitudeFactory.withSections(
            sections = listOf(section),
            passengerId = georgeId,
            status = ACCEPTED
        )
        val martinId = UUID.randomUUID().toString()
        val martinTripLegSolicitude = TripLegSolicitudeFactory.withSections(
            sections = listOf(section),
            passengerId = martinId,
            status = PENDING_APPROVAL
        )
        every { tripRepository.find(commandQuery = TripRepository.CommandQuery(ids = listOf(UUID.fromString(trip.id)))) } returns listOf(
            trip
        )
        givenSectionsAssociatedToATrip(tripId, listOf(section))
        givenTripLegSolicitudeAssociatedToASection(section.id, listOf(georgeTripLegSolicitude, martinTripLegSolicitude))
        givenTripLegSolicitudesPending(tripId, listOf(martinTripLegSolicitude))
        givenTripLegSolicitudesConfirmed(section, listOf(georgeTripLegSolicitude))
        givenUserTrustScoreWithIds(listOf(georgeId, martinId))
        givenAUserWithId(
            georgeId, UserFactory.common(
                id = UUID.fromString(georgeId),
                firstName = "Jorge",
                lastName = "Cabrera"
            )
        )
        givenAUserWithId(
            martinId, UserFactory.common(
                firstName = "Martin",
                lastName = "Bosch",
                id = UUID.fromString(martinId)
            )
        )

        // when
        val driverTripDetail = retrieveDriverTripDetail(
            RetrieveDriverTripDetail.Input(
                tripId = section.tripId,
            )
        )

        // then
        assertEquals(
            listOf(
                DriverSectionDetail(
                    sectionId = SectionFactory.avCabildo_id,
                    departure = TripPointFactory.avCabildo_4853(),
                    arrival = TripPointFactory.avCabildo_20(),
                    occupiedSeats = 0,
                    availableSeats = 4,
                    passengers = listOf(
                        Passenger(
                            id = georgeId,
                            fullName = "Jorge Cabrera",
                            score = 5.0,
                            hasBeenScored = true,
                            profilePhotoUrl = "http//profile.photo.com",
                        )
                    ),
                )
            ),
            driverTripDetail.sectionDetails,
        )
    }

    @Test
    fun `driver section detail passenger not exists`() {
        // given
        val tripId = UUID.randomUUID()
        val trip = TripFactory.common(id = tripId, status = Trip.Status.IN_PROGRESS)
        val section = SectionFactory.avCabildo(
            tripId = tripId
        )
        val georgeId = UUID.randomUUID().toString()
        val georgeTripLegSolicitude = TripLegSolicitudeFactory.withSections(
            sections = listOf(section),
            passengerId = georgeId,
            status = ACCEPTED
        )

        every { tripRepository.find(commandQuery = TripRepository.CommandQuery(ids = listOf(UUID.fromString(trip.id)))) } returns listOf(
            trip
        )
        givenSectionsAssociatedToATrip(tripId, listOf(section))
        givenTripLegSolicitudeAssociatedToASection(section.id, listOf(georgeTripLegSolicitude))
        givenTripLegSolicitudesPending(tripId)
        givenUserTrustScoreWithIds(listOf(georgeId))
        givenAUserWithId(georgeId, null)

        // when
        val driverTripDetail = retrieveDriverTripDetail(
            RetrieveDriverTripDetail.Input(
                tripId = section.tripId,
            )
        )

        // then
        assertEquals(
            listOf(
                DriverSectionDetail(
                    sectionId = SectionFactory.avCabildo_id,
                    departure = TripPointFactory.avCabildo_4853(),
                    arrival = TripPointFactory.avCabildo_20(),
                    occupiedSeats = 0,
                    availableSeats = 4,
                    passengers = listOf(PassengerNotExists(id = georgeId)),
                )
            ),
            driverTripDetail.sectionDetails,
        )
    }


    /*
                            @Test
                            fun `driver section detail passenger not been rated`() {
                                val section = SectionFactory.avCabildo()
                                sectionRepository.save(section)
                                listOf(
                                    TripPlanSolicitudeFactory.withASingleTripApplicationConfirmed(
                                        sections = listOf(section),
                                        passengerId = "PAINN",
                                    ),
                                ).forEach { tripPlanSolicitudeRepository.insert(it) }

                                val driverTripDetail = retrieveDriverTripDetail(
                                    RetrieveDriverTripDetail.Input(
                                        tripId = section.tripId,
                                    )
                                )

                                assertEquals(
                                    listOf(
                                        DriverSectionDetail(
                                            sectionId = SectionFactory.avCabildo_id,
                                            departure = TripPointFactory.avCabildo_4853(),
                                            arrival = TripPointFactory.avCabildo_20(),
                                            occupiedSeats = 0,
                                            availableSeats = 4,
                                            passengers = listOf(
                                                Passenger(
                                                    id = "PAINN",
                                                    fullName = "Painn Wilson",
                                                    rating = NotBeenRated,
                                                    profilePhotoUrl = "",
                                                )
                                            ),
                                        )
                                    ),
                                    driverTripDetail.sectionDetails,
                                )
                            }

                            @Test
                            fun `should not has pending applications when trip not has any application`() {
                                val section = SectionFactory.avCabildo()
                                sectionRepository.save(section)

                                val driverTripDetail = retrieveDriverTripDetail(
                                    RetrieveDriverTripDetail.Input(
                                        tripId = section.tripId,
                                    )
                                )

                                assertFalse(driverTripDetail.hasPendingApplications)
                            }

                            @Test
                            fun `has pending applications only consider applications in pending state`() {
                                val section = SectionFactory.avCabildo()
                                sectionRepository.save(section)
                                listOf(
                                    TripPlanSolicitudeFactory.withASingleTripApplicationPendingApproval(
                                        sections = listOf(section),
                                        passengerId = "JOHN",
                                    ),
                                    TripPlanSolicitudeFactory.withASingleTripApplicationRejected(
                                        sections = listOf(section),
                                        passengerId = "JENNA",
                                    ),
                                    TripPlanSolicitudeFactory.withASingleTripApplicationConfirmed(
                                        sections = listOf(section),
                                        passengerId = "BJNOVAK",
                                    ),
                                ).forEach { tripPlanSolicitudeRepository.insert(it) }

                                val driverTripDetail = retrieveDriverTripDetail(
                                    RetrieveDriverTripDetail.Input(
                                        tripId = section.tripId,
                                    )
                                )

                                assertTrue(driverTripDetail.hasPendingApplications)
                            }
                        */
    private fun givenSectionsAssociatedToATrip(tripId: UUID?, sections: List<Section>) {
        every { sectionRepository.findAllByTripIdOrFail(tripId = match { it == tripId }) } returns sections
    }

    private fun givenTripLegSolicitudesConfirmed(
        section: Section,
        response: List<TripLegSolicitude>
    ) {
        every {
            tripLegSolicitudeRepository.find(commandQuery = match {
                it.sectionId == section.id && it.status == ACCEPTED
            })
        } returns response
    }

    private fun givenTripLegSolicitudesPending(
        tripId: UUID?,
        response: List<TripLegSolicitude> = emptyList()
    ) {
        every { tripLegSolicitudeRepository.find(commandQuery = match { it.tripId == tripId && it.status == PENDING_APPROVAL }) } returns response
    }

    private fun givenTripLegSolicitudeAssociatedToASection(
        sectionId: String,
        response: List<TripLegSolicitude> = emptyList(),
    ) {
        every { tripLegSolicitudeRepository.find(commandQuery = match { it.sectionId == sectionId }) } returns response
    }

    private fun givenUserTrustScoreWithIds(
        ids: List<String>,
        response: UserTrustScore = UserTrustScoreFactory.common()
    ) {
        every { userTrustScoreRepository.findById(match { it in ids }) } returns response
    }

    private fun givenAUserWithId(userId: String, user: User? = null) {
        every { userRepository.findByUserId(match { it == userId }) } returns user
    }
}