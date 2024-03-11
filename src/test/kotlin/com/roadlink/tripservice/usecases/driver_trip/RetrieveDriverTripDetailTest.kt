package com.roadlink.tripservice.usecases.driver_trip

import com.roadlink.tripservice.config.StubTimeProvider
import com.roadlink.tripservice.domain.NotBeenRated
import com.roadlink.tripservice.domain.Rated
import com.roadlink.tripservice.domain.driver_trip.DriverSectionDetail
import com.roadlink.tripservice.domain.driver_trip.Passenger
import com.roadlink.tripservice.domain.driver_trip.PassengerNotExists
import com.roadlink.tripservice.domain.driver_trip.SeatsAvailabilityStatus.*
import com.roadlink.tripservice.domain.trip.TripStatus.*
import com.roadlink.tripservice.infrastructure.persistence.FixedRatingRepository
import com.roadlink.tripservice.infrastructure.persistence.section.InMemorySectionRepository
import com.roadlink.tripservice.infrastructure.persistence.trip_solicitude.InMemoryTripLegSolicitudeRepository
import com.roadlink.tripservice.infrastructure.persistence.trip_solicitude.plan.InMemoryTripPlanSolicitudeRepository
import com.roadlink.tripservice.infrastructure.persistence.user.FixedUserRepository
import com.roadlink.tripservice.usecases.common.TripPointFactory
import com.roadlink.tripservice.usecases.factory.InstantFactory.october15_12hs
import com.roadlink.tripservice.usecases.factory.InstantFactory.october15_13hs
import com.roadlink.tripservice.usecases.factory.InstantFactory.october15_15hs
import com.roadlink.tripservice.usecases.factory.InstantFactory.october15_18hs
import com.roadlink.tripservice.usecases.factory.InstantFactory.october15_7hs
import com.roadlink.tripservice.usecases.factory.SectionFactory
import com.roadlink.tripservice.usecases.factory.builder
import com.roadlink.tripservice.usecases.trip_solicitude.plan.TripPlanSolicitudeFactory
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RetrieveDriverTripDetailTest {

    private lateinit var inMemorySectionRepository: InMemorySectionRepository

    private lateinit var inMemoryTripPlanSolicitudeRepository: InMemoryTripPlanSolicitudeRepository

    private lateinit var inMemoryTripApplicationRepository: InMemoryTripLegSolicitudeRepository

    private lateinit var fixedUserRepository: FixedUserRepository

    private lateinit var fixedRatingRepository: FixedRatingRepository

    private lateinit var stubTimeProvider: StubTimeProvider

    private lateinit var retrieveDriverTripDetail: RetrieveDriverTripDetail

    @BeforeEach
    fun setUp() {
        inMemorySectionRepository = InMemorySectionRepository()
        inMemoryTripApplicationRepository = InMemoryTripLegSolicitudeRepository()
        inMemoryTripPlanSolicitudeRepository = InMemoryTripPlanSolicitudeRepository(
            tripLegSolicitudeRepository = inMemoryTripApplicationRepository
        )
        fixedUserRepository = FixedUserRepository()
        fixedRatingRepository = FixedRatingRepository()
        stubTimeProvider = StubTimeProvider(fixedNow = october15_13hs())

        retrieveDriverTripDetail = RetrieveDriverTripDetail(
            sectionRepository = inMemorySectionRepository,
            tripLegSolicitudeRepository = inMemoryTripApplicationRepository,
            userRepository = fixedUserRepository,
            ratingRepository = fixedRatingRepository,
            timeProvider = stubTimeProvider,
        )
    }

    @Test
    fun `trip not started`() {
        val section = SectionFactory.avCabildo(
            departure = TripPointFactory.avCabildo_4853(at = october15_15hs()),
            arrival = TripPointFactory.avCabildo_20(at = october15_18hs()),
        )
        inMemorySectionRepository.save(section)

        val driverTripDetail = retrieveDriverTripDetail(
            RetrieveDriverTripDetail.Input(
                tripId = section.tripId,
            )
        )

        assertEquals(NOT_STARTED, driverTripDetail.tripStatus)
    }

    @Test
    fun `trip finished`() {
        val section = SectionFactory.avCabildo(
            departure = TripPointFactory.avCabildo_4853(at = october15_7hs()),
            arrival = TripPointFactory.avCabildo_20(at = october15_12hs()),
        )
        inMemorySectionRepository.save(section)

        val driverTripDetail = retrieveDriverTripDetail(
            RetrieveDriverTripDetail.Input(
                tripId = section.tripId,
            )
        )

        assertEquals(FINISHED, driverTripDetail.tripStatus)
    }

    @Test
    fun `given a section that belongs to a trip, when get the driver trip detail then its status must be the expected`() {
        // given
        val section = SectionFactory.avCabildo(
            departure = TripPointFactory.avCabildo_4853(at = october15_12hs()),
            arrival = TripPointFactory.avCabildo_20(at = october15_15hs()),
        )
        inMemorySectionRepository.save(section)

        // when
        val driverTripDetail = retrieveDriverTripDetail(
            RetrieveDriverTripDetail.Input(
                tripId = section.tripId,
            )
        )

        // then
        assertEquals(IN_PROGRESS, driverTripDetail.tripStatus)
    }

    @Test
    fun `no seats available`() {
        val section = SectionFactory.avCabildo().builder()
            .noSeatsAvailable()
            .build()
        inMemorySectionRepository.save(section)

        val driverTripDetail = retrieveDriverTripDetail(
            RetrieveDriverTripDetail.Input(
                tripId = section.tripId,
            )
        )

        assertEquals(NO_SEATS_AVAILABLE, driverTripDetail.seatStatus)
    }

    @Test
    fun `all seats available`() {
        val section = SectionFactory.avCabildo().builder()
            .allSeatsAvailable()
            .build()
        inMemorySectionRepository.save(section)

        val driverTripDetail = retrieveDriverTripDetail(
            RetrieveDriverTripDetail.Input(
                tripId = section.tripId,
            )
        )

        assertEquals(ALL_SEATS_AVAILABLE, driverTripDetail.seatStatus)
    }

    @Test
    fun `some seats available`() {
        val section = SectionFactory.avCabildo().builder()
            .someSeatsAvailable()
            .build()
        inMemorySectionRepository.save(section)

        val driverTripDetail = retrieveDriverTripDetail(
            RetrieveDriverTripDetail.Input(
                tripId = section.tripId
            )
        )

        assertEquals(SOME_SEATS_AVAILABLE, driverTripDetail.seatStatus)
    }

    @Test
    fun `driver section detail no passenger`() {
        val section = SectionFactory.avCabildo()
        inMemorySectionRepository.save(section)

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
                    passengers = emptyList(),
                )
            ),
            driverTripDetail.sectionDetails,
        )
    }

    @Test
    fun `driver section detail only consider passenger who has accepted application`() {
        val section = SectionFactory.avCabildo()
        inMemorySectionRepository.save(section)
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
        ).forEach { inMemoryTripPlanSolicitudeRepository.insert(it) }

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
                            id = "BJNOVAK",
                            fullName = "B.J.Novak",
                            rating = Rated(rating = 2.7),
                        )
                    ),
                )
            ),
            driverTripDetail.sectionDetails,
        )
    }

    @Test
    fun `driver section detail passenger not exists`() {
        val section = SectionFactory.avCabildo()
        inMemorySectionRepository.save(section)
        listOf(
            TripPlanSolicitudeFactory.withASingleTripApplicationConfirmed(
                sections = listOf(section),
                passengerId = "ANGELA",
            ),
        ).forEach { inMemoryTripPlanSolicitudeRepository.insert(it) }

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
                    passengers = listOf(PassengerNotExists(id = "ANGELA")),
                )
            ),
            driverTripDetail.sectionDetails,
        )
    }

    @Test
    fun `driver section detail passenger not been rated`() {
        val section = SectionFactory.avCabildo()
        inMemorySectionRepository.save(section)
        listOf(
            TripPlanSolicitudeFactory.withASingleTripApplicationConfirmed(
                sections = listOf(section),
                passengerId = "PAINN",
            ),
        ).forEach { inMemoryTripPlanSolicitudeRepository.insert(it) }

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
        inMemorySectionRepository.save(section)

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
        inMemorySectionRepository.save(section)
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
        ).forEach { inMemoryTripPlanSolicitudeRepository.insert(it) }

        val driverTripDetail = retrieveDriverTripDetail(
            RetrieveDriverTripDetail.Input(
                tripId = section.tripId,
            )
        )

        assertTrue(driverTripDetail.hasPendingApplications)
    }

}