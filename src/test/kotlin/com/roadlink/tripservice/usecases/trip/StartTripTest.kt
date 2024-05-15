package com.roadlink.tripservice.usecases.trip

import com.roadlink.tripservice.domain.trip.Trip
import com.roadlink.tripservice.domain.trip.TripRepository
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitudeRepository
import com.roadlink.tripservice.usecases.trip_solicitude.plan.TripPlanSolicitudeFactory
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

internal class StartTripTest {

    @MockK
    lateinit var tripPlanSolicitudeRepository: TripPlanSolicitudeRepository

    @MockK
    lateinit var tripRepository: TripRepository

    private lateinit var startTrip: StartTrip

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        startTrip = StartTrip(tripPlanSolicitudeRepository, tripRepository)
    }

    @Test
    fun `when start a trip plan, but there isn't any pending trip plans solicitude, then none solicitudes will be reject`() {
        // given
        val tripId = UUID.randomUUID()
        val rejectedTripPlanSolicitude =
            TripPlanSolicitudeFactory.withASingleTripLegSolicitudeRejected()
        val acceptedTripPlanSolicitude =
            TripPlanSolicitudeFactory.withASingleTripLegSolicitudeAccepted()
        val otherAcceptedTripPlanSolicitude =
            TripPlanSolicitudeFactory.withASingleTripLegSolicitudeAccepted()
        val trip = TripFactory.common(id = tripId)

        every { tripPlanSolicitudeRepository.find(match { it.tripIds.contains(tripId) }) } returns listOf(
            rejectedTripPlanSolicitude,
            acceptedTripPlanSolicitude,
            otherAcceptedTripPlanSolicitude
        )
        every { tripRepository.find(TripRepository.CommandQuery(ids = listOf(UUID.fromString(trip.id)))) } returns listOf(
            trip
        )
        every { tripRepository.insert(trip = match { it.status == Trip.Status.IN_PROGRESS }) } just runs
        every { tripPlanSolicitudeRepository.update(any()) } just runs

        // when
        val response = startTrip(StartTrip.Input(tripId))

        // then
        assertEquals(0, response.rejectedTripPlanSolicitudes.size)
    }

    @Test
    fun `when start a trip plan, then only the pending trip plans solicitude must be reject`() {
        // given
        val tripId = UUID.randomUUID()
        val rejectedTripPlanSolicitude =
            TripPlanSolicitudeFactory.withASingleTripLegSolicitudeRejected()
        val acceptedTripPlanSolicitude =
            TripPlanSolicitudeFactory.withASingleTripLegSolicitudeAccepted()
        val pendingTripPlanSolicitude =
            TripPlanSolicitudeFactory.withASingleTripLegSolicitudePendingApproval()
        val trip = TripFactory.common(id = tripId)

        every { tripPlanSolicitudeRepository.find(match { it.tripIds.contains(tripId) }) } returns listOf(
            rejectedTripPlanSolicitude,
            acceptedTripPlanSolicitude,
            pendingTripPlanSolicitude
        )
        every { tripRepository.find(TripRepository.CommandQuery(ids = listOf(UUID.fromString(trip.id)))) } returns listOf(
            trip
        )
        every { tripRepository.insert(trip = match { it.status == Trip.Status.IN_PROGRESS }) } just runs
        every { tripPlanSolicitudeRepository.update(any()) } just runs

        // when
        val response = startTrip(StartTrip.Input(tripId))

        // then
        assertEquals(1, response.rejectedTripPlanSolicitudes.size)
        assertTrue(
            response.rejectedTripPlanSolicitudes.map { it.id }.contains(
                pendingTripPlanSolicitude.id
            )
        )
    }

    @Test
    fun `when start a trip plan, then all the pending trip plans solicitude must be reject`() {
        // given
        val tripId = UUID.randomUUID()
        val rejectedTripPlanSolicitude =
            TripPlanSolicitudeFactory.withASingleTripLegSolicitudeRejected()
        val twoPendingTripPlanSolicitude =
            TripPlanSolicitudeFactory.withASingleTripLegSolicitudePendingApproval()
        val onePendingTripPlanSolicitude =
            TripPlanSolicitudeFactory.withASingleTripLegSolicitudePendingApproval()
        val trip = TripFactory.common(id = tripId)

        every { tripPlanSolicitudeRepository.find(match { it.tripIds.contains(tripId) }) } returns listOf(
            rejectedTripPlanSolicitude,
            onePendingTripPlanSolicitude,
            twoPendingTripPlanSolicitude
        )
        every { tripRepository.find(TripRepository.CommandQuery(ids = listOf(UUID.fromString(trip.id)))) } returns listOf(
            trip
        )
        every { tripRepository.insert(trip = match { it.status == Trip.Status.IN_PROGRESS }) } just runs
        every { tripPlanSolicitudeRepository.update(any()) } just runs

        // when
        val response = startTrip(StartTrip.Input(tripId))

        // then
        assertEquals(2, response.rejectedTripPlanSolicitudes.size)
        assertTrue(
            response.rejectedTripPlanSolicitudes.map { it.id }.contains(
                onePendingTripPlanSolicitude.id,
            )
        )
        assertTrue(
            response.rejectedTripPlanSolicitudes.map { it.id }.contains(
                twoPendingTripPlanSolicitude.id,
            )
        )
    }
}