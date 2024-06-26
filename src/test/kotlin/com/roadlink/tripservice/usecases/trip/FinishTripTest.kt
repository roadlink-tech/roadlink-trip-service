package com.roadlink.tripservice.usecases.trip

import com.roadlink.tripservice.domain.trip.Trip
import com.roadlink.tripservice.domain.trip.TripRepository
import com.roadlink.tripservice.domain.trip.feedback_solicitude.FeedbackSolicitudeRepository
import com.roadlink.tripservice.domain.trip_plan.TripPlan.Status.FINISHED
import com.roadlink.tripservice.domain.trip_plan.TripPlanRepository
import com.roadlink.tripservice.usecases.trip_plan.TripPlanFactory
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
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

    @MockK
    private lateinit var tripRepository: TripRepository

    @MockK
    private lateinit var feedbackSolicitudeRepository: FeedbackSolicitudeRepository

    private lateinit var finishTrip: FinishTrip

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        finishTrip = FinishTrip(tripPlanRepository, tripRepository, feedbackSolicitudeRepository)
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
        val trip = TripFactory.common(id = tripId)

        every { tripPlanRepository.find(commandQuery = match { it.tripId == tripId }) } returns listOf(
            TripPlanFactory.withASingleTripLeg(
                passengerId = georgeId,
                tripId = tripId,
                driverId = driverId
            ),
            TripPlanFactory.withASingleTripLeg(
                passengerId = martinId,
                tripId = tripId,
                driverId = driverId
            ),
            TripPlanFactory.withASingleTripLeg(
                passengerId = felixId,
                tripId = tripId,
                driverId = driverId
            ),
        )

        every {
            tripPlanRepository.update(match { tripPlan ->
                tripPlan.tripLegs.first { tripLeg ->
                    tripLeg.tripId == tripId
                }.status == FINISHED
            })
        } answers { arg(0) }

        every {
            feedbackSolicitudeRepository.insert(any())
        } just runs

        every { tripRepository.find(TripRepository.CommandQuery(ids = listOf(tripId))) } returns listOf(
            trip
        )
        every { tripRepository.update(trip = match { it.status == Trip.Status.FINISHED }) } just runs

        // when
        val response = finishTrip(FinishTrip.Input(tripId))

        // then
        val feedbackSolicitudesPairs =
            response.feedbackSolicitudes.map { Pair(it.reviewerId, it.receiverId) }
        assertEquals(12, response.feedbackSolicitudes.size)
        verify(exactly = 3) { tripPlanRepository.update(any()) }
        verify(exactly = 12) { feedbackSolicitudeRepository.insert(any()) }

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
        val trip = TripFactory.common(id = tripId)

        every { tripPlanRepository.find(commandQuery = match { it.tripId == tripId }) } returns listOf(
            TripPlanFactory.withASingleTripLeg(
                passengerId = georgeId,
                tripId = tripId,
                driverId = driverId
            ),
        )

        every {
            tripPlanRepository.update(match { tripPlan -> tripPlan.isFinished() })
        } answers { arg(0) }

        every {
            feedbackSolicitudeRepository.insert(any())
        } just runs

        every { tripRepository.find(TripRepository.CommandQuery(ids = listOf(tripId))) } returns listOf(
            trip
        )
        every { tripRepository.update(trip = match { it.status == Trip.Status.FINISHED }) } just runs

        // when
        val response = finishTrip(FinishTrip.Input(tripId))

        // then
        val feedbackSolicitudesPairs =
            response.feedbackSolicitudes.map { Pair(it.reviewerId, it.receiverId) }
        assertEquals(2, response.feedbackSolicitudes.size)
        verify(exactly = 1) { tripPlanRepository.update(any()) }
        verify(exactly = 2) { feedbackSolicitudeRepository.insert(any()) }
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
        val trip = TripFactory.common(id = tripId)

        every { tripPlanRepository.find(commandQuery = match { it.tripId == tripId }) } returns listOf()
        every { tripRepository.find(TripRepository.CommandQuery(ids = listOf(tripId))) } returns listOf(
            trip
        )
        every { tripRepository.update(trip = match { it.status == Trip.Status.FINISHED }) } just runs

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
    fun `when a trip finish, and the plan contains more than one trip leg with driver and passenger, then the expected feedbacks solicitudes must be created`() {
        // given
        val tripId = UUID.randomUUID()
        val otherTripId = UUID.randomUUID()
        val georgeId = UUID.randomUUID()
        val driverId = UUID.randomUUID()
        val otherDriverId = UUID.randomUUID()
        val trip = TripFactory.common(id = tripId)

        every { tripPlanRepository.find(commandQuery = match { it.tripId == tripId }) } returns listOf()
        every { tripRepository.find(TripRepository.CommandQuery(ids = listOf(tripId))) } returns listOf(
            trip
        )
        every { tripRepository.update(trip = match { it.status == Trip.Status.FINISHED }) } just runs
        every { tripPlanRepository.find(commandQuery = match { it.tripId == tripId }) } returns listOf(
            TripPlanFactory.withTwoTripLeg(
                passengerId = georgeId,
                oneTripId = tripId,
                oneDriverId = driverId,
                anotherTripId = otherTripId,
                otherDriverId = otherDriverId
            ),
        )
        every {
            tripPlanRepository.update(match { tripPlan -> !tripPlan.isFinished() })
        } answers { arg(0) }

        every {
            feedbackSolicitudeRepository.insert(any())
        } just runs

        // when
        val response = finishTrip(FinishTrip.Input(tripId))

        // then
        val feedbackSolicitudesPairs =
            response.feedbackSolicitudes.map { Pair(it.reviewerId, it.receiverId) }
        assertEquals(2, response.feedbackSolicitudes.size)
        verify(exactly = 1) { tripPlanRepository.update(any()) }
        verify(exactly = 2) { feedbackSolicitudeRepository.insert(any()) }

        assertTrue(feedbackSolicitudesPairs.contains(Pair(georgeId, driverId)))
        assertFalse(feedbackSolicitudesPairs.contains(Pair(georgeId, georgeId)))

        assertTrue(feedbackSolicitudesPairs.contains(Pair(driverId, georgeId)))
        assertFalse(feedbackSolicitudesPairs.contains(Pair(driverId, driverId)))
    }

    /**
     * Passengers: G
     * --> (G, D)
     * --> (D, G)
     */
    @Test
    fun `when a trip finish, and the plan contains just one trip leg with driver and passenger, then the expected feedbacks solicitudes must be created`() {
        // given
        val tripId = UUID.randomUUID()
        val tripLegId = UUID.randomUUID()
        val georgeId = UUID.randomUUID()
        val driverId = UUID.randomUUID()
        val trip = TripFactory.common(id = tripId)

        every { tripRepository.find(TripRepository.CommandQuery(ids = listOf(tripId))) } returns listOf(
            trip
        )
        every { tripRepository.update(trip = match { it.status == Trip.Status.FINISHED }) } just runs
        every { tripPlanRepository.find(commandQuery = match { it.tripId == tripId }) } returns listOf(
            TripPlanFactory.withASingleTripLeg(
                passengerId = georgeId,
                tripId = tripId,
                driverId = driverId,
                tripLegId = tripLegId
            ),
        )

        every {
            tripPlanRepository.update(match { tripPlan -> tripPlan.isFinished() })
        } answers { arg(0) }

        every {
            feedbackSolicitudeRepository.insert(any())
        } just runs

        // when
        val response = finishTrip(FinishTrip.Input(tripId))

        // then
        val feedbackSolicitudesPairs =
            response.feedbackSolicitudes.map { Pair(it.reviewerId, it.receiverId) }
        assertEquals(2, response.feedbackSolicitudes.size)
        verify(exactly = 1) { tripPlanRepository.update(any()) }
        verify(exactly = 2) { feedbackSolicitudeRepository.insert(any()) }

        assertTrue(feedbackSolicitudesPairs.contains(Pair(georgeId, driverId)))
        assertFalse(feedbackSolicitudesPairs.contains(Pair(georgeId, georgeId)))

        assertTrue(feedbackSolicitudesPairs.contains(Pair(driverId, georgeId)))
        assertFalse(feedbackSolicitudesPairs.contains(Pair(driverId, driverId)))

        assertEquals(listOf(tripLegId), response.tripLegsFinished.map { it.id })
        assertEquals(listOf(FINISHED), response.tripLegsFinished.map { it.status })
    }
}