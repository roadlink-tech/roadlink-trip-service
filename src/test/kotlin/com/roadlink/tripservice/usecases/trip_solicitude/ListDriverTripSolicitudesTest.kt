package com.roadlink.tripservice.usecases.trip_solicitude

import com.roadlink.tripservice.domain.driver_trip.DriverTripLegSolicitude
import com.roadlink.tripservice.domain.driver_trip.Passenger
import com.roadlink.tripservice.domain.trip_solicitude.TripLegSolicitudeRepository
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude.TripLegSolicitude
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude.TripLegSolicitude.Status.*
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitudeRepository
import com.roadlink.tripservice.domain.user.UserRepository
import com.roadlink.tripservice.domain.user.UserTrustScoreRepository
import com.roadlink.tripservice.usecases.common.address.AddressFactory
import com.roadlink.tripservice.usecases.driver_trip.ListDriverTripLegSolicitudes
import com.roadlink.tripservice.usecases.factory.SectionFactory
import com.roadlink.tripservice.usecases.trip.TripFactory
import com.roadlink.tripservice.usecases.trip_solicitude.plan.TripPlanSolicitudeFactory
import com.roadlink.tripservice.usecases.user.UserFactory
import com.roadlink.tripservice.usecases.user.UserTrustScoreFactory
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class ListDriverTripSolicitudesTest {

    @MockK
    private lateinit var tripLegSolicitudeRepository: TripLegSolicitudeRepository

    @MockK
    private lateinit var tripPlanSolicitudeRepository: TripPlanSolicitudeRepository

    @MockK
    private lateinit var userRepository: UserRepository

    @MockK
    private lateinit var userTrustScoreRepository: UserTrustScoreRepository

    private lateinit var listDriverTripLegSolicitudes: ListDriverTripLegSolicitudes

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        listDriverTripLegSolicitudes = ListDriverTripLegSolicitudes(
            tripLegSolicitudeRepository = tripLegSolicitudeRepository,
            userRepository = userRepository,
            userTrustScoreRepository = userTrustScoreRepository,
        )
    }

    @Test
    fun `given no trip applications for the trip, then should return empty list`() {
        // given
        val tripId = UUID.fromString(TripFactory.avCabildo_id)
        every { tripLegSolicitudeRepository.find(TripLegSolicitudeRepository.CommandQuery(tripId = tripId)) } returns emptyList()

        // when
        val driverTripApplications = listDriverTripLegSolicitudes(
            ListDriverTripLegSolicitudes.Input(
                tripId = tripId,
            )
        )

        // then
        assertTrue { driverTripApplications.isEmpty() }
    }

    @Test
    fun `given no pending trip applications for the trip then should return empty list`() {
        // given
        val tripId = UUID.fromString(TripFactory.avCabildo_id)
        val tripLegSolicitude = TripLegSolicitude(
            id = UUID.randomUUID(),
            sections = listOf(SectionFactory.avCabildo(tripId = tripId)),
            passengerId = "passengerId",
            status = CONFIRMED,
            authorizerId = "authorizerId"
        )
        every { tripLegSolicitudeRepository.find(any()) } returns listOf(tripLegSolicitude)

        // when
        val driverTripApplications = listDriverTripLegSolicitudes(
            ListDriverTripLegSolicitudes.Input(
                tripId = tripId,
            )
        )

        // then
        assertTrue { driverTripApplications.isEmpty() }
    }

    @Test
    fun `driver trip plan solicitudes only consider solicitudes which has pending approval`() {
        // given
        val tripId = UUID.fromString(TripFactory.avCabildo_id)
        val userId = UUID.randomUUID()
        val section = SectionFactory.avCabildo(tripId = tripId)
        val pendingApprovalTripLegSolicitude = TripLegSolicitude(
            id = UUID.randomUUID(),
            sections = listOf(section),
            passengerId = "passengerId",
            status = PENDING_APPROVAL,
            authorizerId = "authorizerId"
        )
        val rejectedTripLegSolicitude = TripLegSolicitude(
            id = UUID.randomUUID(),
            sections = listOf(section),
            passengerId = "passengerId",
            status = REJECTED,
            authorizerId = "authorizerId"
        )
        val confirmedTripLegSolicitude = TripLegSolicitude(
            id = UUID.randomUUID(),
            sections = listOf(section),
            passengerId = "passengerId",
            status = CONFIRMED,
            authorizerId = "authorizerId"
        )

        val tripPlanSolicitudePendingApproval =
            TripPlanSolicitudeFactory.withASingleTripLegSolicitude(pendingApprovalTripLegSolicitude)
        val tripPlanSolicitudeRejected =
            TripPlanSolicitudeFactory.withASingleTripLegSolicitude(rejectedTripLegSolicitude)
        val tripPlanSolicitudeConfirmed =
            TripPlanSolicitudeFactory.withASingleTripLegSolicitude(confirmedTripLegSolicitude)

        val tripPlanSolicitudes = listOf(
            tripPlanSolicitudePendingApproval,
            tripPlanSolicitudeRejected,
            tripPlanSolicitudeConfirmed,
        )

        every { tripPlanSolicitudeRepository.find(any()) } returns tripPlanSolicitudes
        every { tripLegSolicitudeRepository.find(any()) } returns listOf(
            pendingApprovalTripLegSolicitude,
            rejectedTripLegSolicitude,
            confirmedTripLegSolicitude
        )
        every { userRepository.findByUserId(any()) } returns UserFactory.common(id = userId)
        every { userTrustScoreRepository.findById(any()) } returns UserTrustScoreFactory.common(score = 1.3)

        // when
        val driverTripApplications = listDriverTripLegSolicitudes(
            ListDriverTripLegSolicitudes.Input(
                tripId = tripId,
            )
        )

        // then
        assertEquals(
            listOf(
                DriverTripLegSolicitude(
                    tripLegSolicitudeId = tripPlanSolicitudePendingApproval.tripLegSolicitudes.first().id,
                    passenger = Passenger(
                        id = userId.toString(),
                        fullName = "John Krasinski",
                        score = 1.3,
                        hasBeenScored = true,
                        profilePhotoUrl = "http//profile.photo.com",
                    ),
                    status = PENDING_APPROVAL,
                    addressJoinStart = AddressFactory.avCabildo_4853(),
                    addressJoinEnd = AddressFactory.avCabildo_20(),
                )
            ),
            driverTripApplications
        )
    }

}