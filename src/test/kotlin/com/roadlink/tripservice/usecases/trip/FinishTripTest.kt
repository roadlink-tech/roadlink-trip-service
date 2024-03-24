package com.roadlink.tripservice.usecases.trip

import com.roadlink.tripservice.domain.trip_plan.TripPlan.Status.FINISHED
import com.roadlink.tripservice.domain.trip_plan.TripPlanRepository
import com.roadlink.tripservice.usecases.trip_plan.TripPlanFactory
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class FinishTripTest {

    @MockK
    private lateinit var tripPlanRepository: TripPlanRepository

    private lateinit var finishTrip: FinishTrip

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        finishTrip = FinishTrip(tripPlanRepository)
    }

    /**
     * Passengers: G | M | F
     * --> (G, M) (G, F) (G, D)
     * --> (M, G) (M, F) (M, D)
     * --> (F, G) (F, M) (F, D)
     * --> (D, G) (D, M) (D, F)
     */
    @Test
    fun `when a trip finish, then all the feedbacks solicitudes must be created`() {
        // given
        val tripId = UUID.randomUUID()
        val georgeId = UUID.randomUUID()
        val martinId = UUID.randomUUID()
        val felixId = UUID.randomUUID()
        val driverId = UUID.randomUUID()

        every { tripPlanRepository.find(commandQuery = match { it.tripId == tripId }) } returns listOf(
            TripPlanFactory.common(passengerId = georgeId, tripId = tripId, driverId = driverId),
            TripPlanFactory.common(passengerId = martinId, tripId = tripId, driverId = driverId),
            TripPlanFactory.common(passengerId = felixId, tripId = tripId, driverId = driverId),
        )

        every {
            tripPlanRepository.update(match { tripPlan ->
                tripPlan.tripLegs.first { tripLeg ->
                    tripLeg.tripId == tripId
                }.status == FINISHED
            })
        } answers { arg(0) }

        // when
        val response = finishTrip(FinishTrip.Input(tripId))

        // then
        val feedbackSolicitudesPairs =
            response.feedbackSolicitudes.map { Pair(it.reviewerId, it.receiverId) }
        assertEquals(12, response.feedbackSolicitudes.size)
        verify(exactly = 3) { tripPlanRepository.update(any()) }

        assertTrue(feedbackSolicitudesPairs.contains(Pair(georgeId, martinId)))
        assertTrue(feedbackSolicitudesPairs.contains(Pair(georgeId, felixId)))
        assertTrue(feedbackSolicitudesPairs.contains(Pair(georgeId, driverId)))
        assertFalse(feedbackSolicitudesPairs.contains(Pair(georgeId, georgeId)))

        assertTrue(feedbackSolicitudesPairs.contains(Pair(martinId, georgeId)))
        assertTrue(feedbackSolicitudesPairs.contains(Pair(martinId, felixId)))
        assertTrue(feedbackSolicitudesPairs.contains(Pair(martinId, driverId)))
        assertFalse(feedbackSolicitudesPairs.contains(Pair(martinId, martinId)))

        assertTrue(feedbackSolicitudesPairs.contains(Pair(felixId, georgeId)))
        assertTrue(feedbackSolicitudesPairs.contains(Pair(felixId, martinId)))
        assertTrue(feedbackSolicitudesPairs.contains(Pair(felixId, driverId)))
        assertFalse(feedbackSolicitudesPairs.contains(Pair(felixId, felixId)))

        assertTrue(feedbackSolicitudesPairs.contains(Pair(driverId, georgeId)))
        assertTrue(feedbackSolicitudesPairs.contains(Pair(driverId, martinId)))
        assertTrue(feedbackSolicitudesPairs.contains(Pair(driverId, felixId)))
        assertFalse(feedbackSolicitudesPairs.contains(Pair(driverId, driverId)))
    }

    /**
     * Passengers: G
     * --> (G, D)
     * --> (D, G)
     */
    @Test
    fun `when a trip finish, and the plan just contains a single trip leg with driver and passenger, then the expected feedbacks solicitudes must be created and the plan must be FINISHED`() {
        // given
        val tripId = UUID.randomUUID()
        val georgeId = UUID.randomUUID()
        val driverId = UUID.randomUUID()

        every { tripPlanRepository.find(commandQuery = match { it.tripId == tripId }) } returns listOf(
            TripPlanFactory.common(passengerId = georgeId, tripId = tripId, driverId = driverId),
        )

        every {
            tripPlanRepository.update(match { tripPlan -> tripPlan.isFinished() })
        } answers { arg(0) }

        // when
        val response = finishTrip(FinishTrip.Input(tripId))

        // then
        val feedbackSolicitudesPairs =
            response.feedbackSolicitudes.map { Pair(it.reviewerId, it.receiverId) }
        assertEquals(2, response.feedbackSolicitudes.size)
        verify(exactly = 1) { tripPlanRepository.update(any()) }

        assertTrue(feedbackSolicitudesPairs.contains(Pair(georgeId, driverId)))
        assertFalse(feedbackSolicitudesPairs.contains(Pair(georgeId, georgeId)))

        assertTrue(feedbackSolicitudesPairs.contains(Pair(driverId, georgeId)))
        assertFalse(feedbackSolicitudesPairs.contains(Pair(driverId, driverId)))
    }

    /**
     * Passengers:
     */
    @Test
    fun `when a trip finish and there isn't any passenger, then none feedbacks solicitudes must be created`() {
        // given
        val tripId = UUID.randomUUID()

        every { tripPlanRepository.find(commandQuery = match { it.tripId == tripId }) } returns listOf()

        // when
        val response = finishTrip(FinishTrip.Input(tripId))

        // then
        assertEquals(0, response.feedbackSolicitudes.size)
        verify(exactly = 0) { tripPlanRepository.update(any()) }
    }

    /**
     * Passengers: G
     * --> (G, D)
     * --> (D, G)
     */
    @Test
    fun `when a trip finish, and the plan just contains more than one trip leg with driver and passenger, then the expected feedbacks solicitudes must be created`() {
        // given
        val tripId = UUID.randomUUID()
        val otherTripId = UUID.randomUUID()
        val georgeId = UUID.randomUUID()
        val driverId = UUID.randomUUID()
        val otherDriverId = UUID.randomUUID()

        every { tripPlanRepository.find(commandQuery = match { it.tripId == tripId }) } returns listOf(
            TripPlanFactory.withTwoTripLeg(
                passengerId = georgeId,
                oneTripId = tripId,
                oneDriverId = driverId,
                anotherTripId = otherTripId,
                anotherDriverId = otherDriverId
            ),
        )

        every {
            tripPlanRepository.update(match { tripPlan -> !tripPlan.isFinished() })
        } answers { arg(0) }

        // when
        val response = finishTrip(FinishTrip.Input(tripId))

        // then
        val feedbackSolicitudesPairs =
            response.feedbackSolicitudes.map { Pair(it.reviewerId, it.receiverId) }
        assertEquals(2, response.feedbackSolicitudes.size)
        verify(exactly = 1) { tripPlanRepository.update(any()) }

        assertTrue(feedbackSolicitudesPairs.contains(Pair(georgeId, driverId)))
        assertFalse(feedbackSolicitudesPairs.contains(Pair(georgeId, georgeId)))

        assertTrue(feedbackSolicitudesPairs.contains(Pair(driverId, georgeId)))
        assertFalse(feedbackSolicitudesPairs.contains(Pair(driverId, driverId)))
    }
}