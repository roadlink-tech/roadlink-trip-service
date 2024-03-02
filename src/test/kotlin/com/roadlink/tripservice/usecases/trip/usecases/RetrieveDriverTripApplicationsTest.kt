package com.roadlink.tripservice.usecases.trip.usecases

import com.roadlink.tripservice.domain.driver_trip.DriverTripApplication
import com.roadlink.tripservice.domain.Passenger
import com.roadlink.tripservice.domain.Rated
import com.roadlink.tripservice.domain.trip_application.TripPlanApplication
import com.roadlink.tripservice.infrastructure.persistence.FixedRatingRepository
import com.roadlink.tripservice.infrastructure.persistence.FixedUserRepository
import com.roadlink.tripservice.infrastructure.persistence.trip_application.InMemoryTripApplicationRepository
import com.roadlink.tripservice.infrastructure.persistence.trip_application.InMemoryTripPlanApplicationRepository
import com.roadlink.tripservice.usecases.trip.domain.AddressFactory
import com.roadlink.tripservice.usecases.trip.domain.SectionFactory
import com.roadlink.tripservice.usecases.trip.domain.TripFactory
import com.roadlink.tripservice.usecases.trip.domain.TripPlanApplicationFactory
import com.roadlink.tripservice.usecases.trip_application.RetrieveDriverTripApplications
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class RetrieveDriverTripApplicationsTest {

    private lateinit var inMemoryTripApplicationRepository: InMemoryTripApplicationRepository

    private lateinit var inMemoryTripPlanApplicationRepository: InMemoryTripPlanApplicationRepository

    private lateinit var fixedUserRepository: FixedUserRepository

    private lateinit var fixedRatingRepository: FixedRatingRepository

    private lateinit var retrieveDriverTripApplications: RetrieveDriverTripApplications

    @BeforeEach
    fun setUp() {
        inMemoryTripApplicationRepository = InMemoryTripApplicationRepository()
        inMemoryTripPlanApplicationRepository = InMemoryTripPlanApplicationRepository(
            tripApplicationRepository = inMemoryTripApplicationRepository
        )
        fixedUserRepository = FixedUserRepository()
        fixedRatingRepository = FixedRatingRepository()

        retrieveDriverTripApplications = RetrieveDriverTripApplications(
            tripApplicationRepository = inMemoryTripApplicationRepository,
            userRepository = fixedUserRepository,
            ratingRepository = fixedRatingRepository,
        )
    }

    @Test
    fun `given no trip applications for the trip then should return empty list`() {
        val tripId = UUID.fromString(TripFactory.avCabildo_id)

        val driverTripApplications = retrieveDriverTripApplications(
            RetrieveDriverTripApplications.Input(
            tripId = tripId,
        ))

        assertTrue { driverTripApplications.isEmpty() }
    }

    @Test
    fun `given no pending trip applications for the trip then should return empty list`() {
        val tripId = UUID.fromString(TripFactory.avCabildo_id)
        val section = SectionFactory.avCabildo(tripId = tripId)
        listOf(
            TripPlanApplicationFactory.withASingleTripApplicationRejected(
                sections = listOf(section),
                passengerId = "JENNA",
            ),
            TripPlanApplicationFactory.withASingleTripApplicationConfirmed(
                sections = listOf(section),
                passengerId = "BJNOVAK",
            ),
        ).forEach { inMemoryTripPlanApplicationRepository.insert(it) }

        val driverTripApplications = retrieveDriverTripApplications(
            RetrieveDriverTripApplications.Input(
                tripId = tripId,
            ))

        assertTrue { driverTripApplications.isEmpty() }
    }

    @Test
    fun `driver trip applications only consider applications who has pending approval`() {
        val tripId = UUID.fromString(TripFactory.avCabildo_id)
        val section = SectionFactory.avCabildo(tripId = tripId)
        val tripPlanApplicationPendingApproval = TripPlanApplicationFactory.withASingleTripApplicationPendingApproval(
            sections = listOf(section),
            passengerId = "JOHN",
        )
        val tripPlanApplicationRejected = TripPlanApplicationFactory.withASingleTripApplicationRejected(
            sections = listOf(section),
            passengerId = "JENNA",
        )
        val tripPlanApplicationConfirmed = TripPlanApplicationFactory.withASingleTripApplicationConfirmed(
            sections = listOf(section),
            passengerId = "BJNOVAK",
        )
        listOf(
            tripPlanApplicationPendingApproval,
            tripPlanApplicationRejected,
            tripPlanApplicationConfirmed,
        ).forEach { inMemoryTripPlanApplicationRepository.insert(it) }

        val driverTripApplications = retrieveDriverTripApplications(
            RetrieveDriverTripApplications.Input(
                tripId = tripId,
            ))

        assertEquals(
            listOf(
                DriverTripApplication(
                    tripApplicationId = tripPlanApplicationPendingApproval.tripApplications.first().id,
                    passenger = Passenger(
                        id = "JOHN",
                        fullName = "John Krasinski",
                        rating = Rated(rating = 1.3),
                    ),
                    applicationStatus = TripPlanApplication.TripApplication.Status.PENDING_APPROVAL,
                    addressJoinStart = AddressFactory.avCabildo_4853(),
                    addressJoinEnd = AddressFactory.avCabildo_20(),
                )
            ),
            driverTripApplications
        )
    }
}