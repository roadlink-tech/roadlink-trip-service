package com.roadlink.tripservice.usecases.trip_solicitude

import com.roadlink.tripservice.domain.driver_trip.DriverTripApplication
import com.roadlink.tripservice.domain.driver_trip.Passenger
import com.roadlink.tripservice.domain.Rated
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude
import com.roadlink.tripservice.infrastructure.persistence.FixedRatingRepository
import com.roadlink.tripservice.infrastructure.persistence.user.FixedUserRepository
import com.roadlink.tripservice.infrastructure.persistence.trip_application.InMemoryTripLegSolicitudeRepository
import com.roadlink.tripservice.infrastructure.persistence.trip_application.plan.InMemoryTripPlanSolicitudeRepository
import com.roadlink.tripservice.usecases.common.address.AddressFactory
import com.roadlink.tripservice.usecases.driver_trip.ListDriverTripApplications
import com.roadlink.tripservice.usecases.factory.SectionFactory
import com.roadlink.tripservice.usecases.trip.TripFactory
import com.roadlink.tripservice.usecases.trip_solicitude.plan.TripPlanSolicitudeFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class ListDriverTripSolicitudesTest {

    private lateinit var inMemoryTripApplicationRepository: InMemoryTripLegSolicitudeRepository

    private lateinit var inMemorytripPlanSolicitudeRepository: InMemoryTripPlanSolicitudeRepository

    private lateinit var fixedUserRepository: FixedUserRepository

    private lateinit var fixedRatingRepository: FixedRatingRepository

    private lateinit var listDriverTripApplications: ListDriverTripApplications

    @BeforeEach
    fun setUp() {
        inMemoryTripApplicationRepository = InMemoryTripLegSolicitudeRepository()
        inMemorytripPlanSolicitudeRepository = InMemoryTripPlanSolicitudeRepository(
            tripLegSolicitudeRepository = inMemoryTripApplicationRepository
        )
        fixedUserRepository = FixedUserRepository()
        fixedRatingRepository = FixedRatingRepository()

        listDriverTripApplications = ListDriverTripApplications(
            tripLegSolicitudeRepository = inMemoryTripApplicationRepository,
            userRepository = fixedUserRepository,
            ratingRepository = fixedRatingRepository,
        )
    }

    @Test
    fun `given no trip applications for the trip then should return empty list`() {
        val tripId = UUID.fromString(TripFactory.avCabildo_id)

        val driverTripApplications = listDriverTripApplications(
            ListDriverTripApplications.Input(
            tripId = tripId,
        ))

        assertTrue { driverTripApplications.isEmpty() }
    }

    @Test
    fun `given no pending trip applications for the trip then should return empty list`() {
        val tripId = UUID.fromString(TripFactory.avCabildo_id)
        val section = SectionFactory.avCabildo(tripId = tripId)
        listOf(
            TripPlanSolicitudeFactory.withASingleTripApplicationRejected(
                sections = listOf(section),
                passengerId = "JENNA",
            ),
            TripPlanSolicitudeFactory.withASingleTripApplicationConfirmed(
                sections = listOf(section),
                passengerId = "BJNOVAK",
            ),
        ).forEach { inMemorytripPlanSolicitudeRepository.insert(it) }

        val driverTripApplications = listDriverTripApplications(
            ListDriverTripApplications.Input(
                tripId = tripId,
            ))

        assertTrue { driverTripApplications.isEmpty() }
    }

    @Test
    fun `driver trip applications only consider applications who has pending approval`() {
        val tripId = UUID.fromString(TripFactory.avCabildo_id)
        val section = SectionFactory.avCabildo(tripId = tripId)
        val tripPlanSolicitudePendingApproval = TripPlanSolicitudeFactory.withASingleTripApplicationPendingApproval(
            sections = listOf(section),
            passengerId = "JOHN",
        )
        val tripPlanSolicitudeRejected = TripPlanSolicitudeFactory.withASingleTripApplicationRejected(
            sections = listOf(section),
            passengerId = "JENNA",
        )
        val tripPlanSolicitudeConfirmed = TripPlanSolicitudeFactory.withASingleTripApplicationConfirmed(
            sections = listOf(section),
            passengerId = "BJNOVAK",
        )
        listOf(
            tripPlanSolicitudePendingApproval,
            tripPlanSolicitudeRejected,
            tripPlanSolicitudeConfirmed,
        ).forEach { inMemorytripPlanSolicitudeRepository.insert(it) }

        val driverTripApplications = listDriverTripApplications(
            ListDriverTripApplications.Input(
                tripId = tripId,
            ))

        assertEquals(
            listOf(
                DriverTripApplication(
                    tripApplicationId = tripPlanSolicitudePendingApproval.tripLegSolicitudes.first().id,
                    passenger = Passenger(
                        id = "JOHN",
                        fullName = "John Krasinski",
                        rating = Rated(rating = 1.3),
                    ),
                    applicationStatus = TripPlanSolicitude.TripLegSolicitude.Status.PENDING_APPROVAL,
                    addressJoinStart = AddressFactory.avCabildo_4853(),
                    addressJoinEnd = AddressFactory.avCabildo_20(),
                )
            ),
            driverTripApplications
        )
    }
}