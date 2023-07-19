package com.roadlink.tripservice.trip.usecases

import com.roadlink.tripservice.domain.*
import com.roadlink.tripservice.infrastructure.persistence.FixedRatingRepository
import com.roadlink.tripservice.infrastructure.persistence.FixedUserRepository
import com.roadlink.tripservice.infrastructure.persistence.InMemorySectionRepository
import com.roadlink.tripservice.infrastructure.persistence.trip_application.InMemoryTripPlanApplicationRepository
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
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetDriverTripDetailTest {

    private lateinit var inMemorySectionRepository: InMemorySectionRepository

    private lateinit var inMemoryTripPlanApplicationRepository: InMemoryTripPlanApplicationRepository

    private lateinit var fixedUserRepository: FixedUserRepository

    private lateinit var fixedRatingRepository: FixedRatingRepository

    private lateinit var stubTimeProvider: StubTimeProvider

    private lateinit var getDriverTripDetail: GetDriverTripDetail

    @BeforeEach
    fun setUp() {
        inMemorySectionRepository = InMemorySectionRepository()
        inMemoryTripPlanApplicationRepository = InMemoryTripPlanApplicationRepository()
        fixedUserRepository = FixedUserRepository()
        fixedRatingRepository = FixedRatingRepository()
        stubTimeProvider = StubTimeProvider(fixedNow = october15_13hs())

        getDriverTripDetail = GetDriverTripDetail(
            sectionRepository = inMemorySectionRepository,
            tripPlanApplicationRepository = inMemoryTripPlanApplicationRepository,
            userRepository = fixedUserRepository,
            ratingRepository = fixedRatingRepository,
            timeProvider = stubTimeProvider,
        )
    }

    @Test
    fun `trip not started`() {
        inMemorySectionRepository.save(SectionFactory.avCabildo(
            departure = TripPointFactory.avCabildo_4853(at = october15_15hs()),
            arrival = TripPointFactory.avCabildo_20(at = october15_18hs()),
        ))

        val driverTripDetail = getDriverTripDetail(
            GetDriverTripDetail.Input(
                tripId = TripFactory.avCabildo_id,
        ))

        assertEquals(NOT_STARTED, driverTripDetail.tripStatus)
    }

    @Test
    fun `trip finished`() {
        inMemorySectionRepository.save(SectionFactory.avCabildo(
            departure = TripPointFactory.avCabildo_4853(at = october15_7hs()),
            arrival = TripPointFactory.avCabildo_20(at = october15_12hs()),
        ))

        val driverTripDetail = getDriverTripDetail(
            GetDriverTripDetail.Input(
                tripId = TripFactory.avCabildo_id,
            ))

        assertEquals(FINISHED, driverTripDetail.tripStatus)
    }

    @Test
    fun `trip in progress`() {
        inMemorySectionRepository.save(SectionFactory.avCabildo(
            departure = TripPointFactory.avCabildo_4853(at = october15_12hs()),
            arrival = TripPointFactory.avCabildo_20(at = october15_15hs()),
        ))

        val driverTripDetail = getDriverTripDetail(
            GetDriverTripDetail.Input(
                tripId = TripFactory.avCabildo_id,
            ))

        assertEquals(IN_PROGRESS, driverTripDetail.tripStatus)
    }

    @Test
    fun `no seats available`() {
        inMemorySectionRepository.save(
            SectionFactory.avCabildo().builder()
                .noSeatsAvailable()
                .build()
        )

        val driverTripDetail = getDriverTripDetail(
            GetDriverTripDetail.Input(
                tripId = TripFactory.avCabildo_id,
            ))

        assertEquals(NO_SEATS_AVAILABLE, driverTripDetail.seatStatus)
    }

    @Test
    fun `all seats available`() {
        inMemorySectionRepository.save(
            SectionFactory.avCabildo().builder()
                .allSeatsAvailable()
                .build()
        )

        val driverTripDetail = getDriverTripDetail(
            GetDriverTripDetail.Input(
                tripId = TripFactory.avCabildo_id,
            ))

        assertEquals(ALL_SEATS_AVAILABLE, driverTripDetail.seatStatus)
    }

    @Test
    fun `some seats available`() {
        inMemorySectionRepository.save(
            SectionFactory.avCabildo().builder()
                .someSeatsAvailable()
                .build()
        )

        val driverTripDetail = getDriverTripDetail(
            GetDriverTripDetail.Input(
                tripId = TripFactory.avCabildo_id,
            ))

        assertEquals(SOME_SEATS_AVAILABLE, driverTripDetail.seatStatus)
    }

    @Test
    fun `driver section detail no passenger`() {
        inMemorySectionRepository.save(SectionFactory.avCabildo())

        val driverTripDetail = getDriverTripDetail(
            GetDriverTripDetail.Input(
                tripId = TripFactory.avCabildo_id,
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
        inMemorySectionRepository.save(SectionFactory.avCabildo())
        listOf(
            TripPlanApplicationFactory.withASingleTripApplicationPendingApproval(
                sections = setOf(SectionFactory.avCabildo()),
                passengerId = "JOHN",
            ),
            TripPlanApplicationFactory.withASingleTripApplicationRejected(
                sections = setOf(SectionFactory.avCabildo()),
                passengerId = "JENNA",
            ),
            TripPlanApplicationFactory.withASingleTripApplicationConfirmed(
                sections = setOf(SectionFactory.avCabildo()),
                passengerId = "BJNOVAK",
            ),
        ).forEach { inMemoryTripPlanApplicationRepository.save(it) }

        val driverTripDetail = getDriverTripDetail(
            GetDriverTripDetail.Input(
                tripId = TripFactory.avCabildo_id,
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
        inMemorySectionRepository.save(SectionFactory.avCabildo())
        listOf(
            TripPlanApplicationFactory.withASingleTripApplicationConfirmed(
                sections = setOf(SectionFactory.avCabildo()),
                passengerId = "ANGELA",
            ),
        ).forEach { inMemoryTripPlanApplicationRepository.save(it) }

        val driverTripDetail = getDriverTripDetail(
            GetDriverTripDetail.Input(
                tripId = TripFactory.avCabildo_id,
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
        inMemorySectionRepository.save(SectionFactory.avCabildo())
        listOf(
            TripPlanApplicationFactory.withASingleTripApplicationConfirmed(
                sections = setOf(SectionFactory.avCabildo()),
                passengerId = "PAINN",
            ),
        ).forEach { inMemoryTripPlanApplicationRepository.save(it) }

        val driverTripDetail = getDriverTripDetail(
            GetDriverTripDetail.Input(
                tripId = TripFactory.avCabildo_id,
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
}