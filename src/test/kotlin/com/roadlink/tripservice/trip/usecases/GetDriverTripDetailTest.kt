package com.roadlink.tripservice.trip.usecases

import com.roadlink.tripservice.domain.*
import com.roadlink.tripservice.infrastructure.persistence.FixedRatingRepository
import com.roadlink.tripservice.infrastructure.persistence.FixedUserRepository
import com.roadlink.tripservice.trip.StubTimeProvider
import com.roadlink.tripservice.trip.domain.*
import com.roadlink.tripservice.trip.domain.InstantFactory.october15_12hs
import com.roadlink.tripservice.trip.domain.InstantFactory.october15_13hs
import com.roadlink.tripservice.trip.domain.InstantFactory.october15_15hs
import com.roadlink.tripservice.trip.domain.InstantFactory.october15_18hs
import com.roadlink.tripservice.trip.domain.InstantFactory.october15_7hs
import com.roadlink.tripservice.usecases.GetDriverTripDetail
import com.roadlink.tripservice.domain.SeatsAvailabilityStatus.*
import com.roadlink.tripservice.domain.TripStatus.*
import com.roadlink.tripservice.infrastructure.persistence.InMemorySectionRepository
import com.roadlink.tripservice.infrastructure.persistence.trip_application.InMemoryTripApplicationRepository
import com.roadlink.tripservice.infrastructure.persistence.trip_application.InMemoryTripPlanApplicationRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetDriverTripDetailTest {

    private lateinit var inMemorySectionRepository: InMemorySectionRepository

    private lateinit var inMemoryTripPlanApplicationRepository: InMemoryTripPlanApplicationRepository

    private lateinit var inMemoryTripApplicationRepository: InMemoryTripApplicationRepository

    private lateinit var fixedUserRepository: FixedUserRepository

    private lateinit var fixedRatingRepository: FixedRatingRepository

    private lateinit var stubTimeProvider: StubTimeProvider

    private lateinit var getDriverTripDetail: GetDriverTripDetail

    @BeforeEach
    fun setUp() {
        inMemorySectionRepository = InMemorySectionRepository()
        inMemoryTripApplicationRepository = InMemoryTripApplicationRepository()
        inMemoryTripPlanApplicationRepository = InMemoryTripPlanApplicationRepository(
            tripApplicationRepository = inMemoryTripApplicationRepository
        )
        fixedUserRepository = FixedUserRepository()
        fixedRatingRepository = FixedRatingRepository()
        stubTimeProvider = StubTimeProvider(fixedNow = october15_13hs())

        getDriverTripDetail = GetDriverTripDetail(
            sectionRepository = inMemorySectionRepository,
            tripPlanApplicationRepository = inMemoryTripPlanApplicationRepository,
            tripApplicationRepository = inMemoryTripApplicationRepository,
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

        val driverTripDetail = getDriverTripDetail(
            GetDriverTripDetail.Input(
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

        val driverTripDetail = getDriverTripDetail(
            GetDriverTripDetail.Input(
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
        val driverTripDetail = getDriverTripDetail(
            GetDriverTripDetail.Input(
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

        val driverTripDetail = getDriverTripDetail(
            GetDriverTripDetail.Input(
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

        val driverTripDetail = getDriverTripDetail(
            GetDriverTripDetail.Input(
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

        val driverTripDetail = getDriverTripDetail(
            GetDriverTripDetail.Input(
                tripId = section.tripId
            )
        )

        assertEquals(SOME_SEATS_AVAILABLE, driverTripDetail.seatStatus)
    }

    @Test
    fun `driver section detail no passenger`() {
        val section = SectionFactory.avCabildo()
        inMemorySectionRepository.save(section)

        val driverTripDetail = getDriverTripDetail(
            GetDriverTripDetail.Input(
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
            TripPlanApplicationFactory.withASingleTripApplicationPendingApproval(
                sections = listOf(section),
                passengerId = "JOHN",
            ),
            TripPlanApplicationFactory.withASingleTripApplicationRejected(
                sections = listOf(section),
                passengerId = "JENNA",
            ),
            TripPlanApplicationFactory.withASingleTripApplicationConfirmed(
                sections = listOf(section),
                passengerId = "BJNOVAK",
            ),
        ).forEach { inMemoryTripPlanApplicationRepository.insert(it) }

        val driverTripDetail = getDriverTripDetail(
            GetDriverTripDetail.Input(
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
            TripPlanApplicationFactory.withASingleTripApplicationConfirmed(
                sections = listOf(section),
                passengerId = "ANGELA",
            ),
        ).forEach { inMemoryTripPlanApplicationRepository.insert(it) }

        val driverTripDetail = getDriverTripDetail(
            GetDriverTripDetail.Input(
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
            TripPlanApplicationFactory.withASingleTripApplicationConfirmed(
                sections = listOf(section),
                passengerId = "PAINN",
            ),
        ).forEach { inMemoryTripPlanApplicationRepository.insert(it) }

        val driverTripDetail = getDriverTripDetail(
            GetDriverTripDetail.Input(
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

        val driverTripDetail = getDriverTripDetail(
            GetDriverTripDetail.Input(
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
            TripPlanApplicationFactory.withASingleTripApplicationPendingApproval(
                sections = listOf(section),
                passengerId = "JOHN",
            ),
            TripPlanApplicationFactory.withASingleTripApplicationRejected(
                sections = listOf(section),
                passengerId = "JENNA",
            ),
            TripPlanApplicationFactory.withASingleTripApplicationConfirmed(
                sections = listOf(section),
                passengerId = "BJNOVAK",
            ),
        ).forEach { inMemoryTripPlanApplicationRepository.insert(it) }

        val driverTripDetail = getDriverTripDetail(
            GetDriverTripDetail.Input(
                tripId = section.tripId,
            )
        )

        assertTrue(driverTripDetail.hasPendingApplications)
    }

}