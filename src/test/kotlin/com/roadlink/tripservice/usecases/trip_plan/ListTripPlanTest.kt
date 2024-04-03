package com.roadlink.tripservice.usecases.trip_plan

import com.roadlink.tripservice.domain.trip_plan.TripPlan.Status.*
import com.roadlink.tripservice.domain.trip_plan.TripPlanRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class ListTripPlanTest {

    @MockK
    lateinit var tripPlanRepository: TripPlanRepository

    private lateinit var listTripPlan: ListTripPlan

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        listTripPlan = ListTripPlan(tripPlanRepository)
    }

    @Test
    fun `list trip plans by passenger id and status`() {
        // given
        val passengerId = UUID.randomUUID()
        val tripPlanNotFinished =
            TripPlanFactory.withASingleTripLeg(passengerId = passengerId, status = NOT_FINISHED)
        val tripPlanCancelled = TripPlanFactory.withASingleTripLeg(passengerId = passengerId, status = CANCELLED)
        every { tripPlanRepository.find(commandQuery = match { it.passengerId == passengerId }) } returns listOf(
            tripPlanNotFinished,
            tripPlanCancelled
        )

        // when
        val response =
            listTripPlan(ListTripPlan.Input(passengerId = passengerId, status = listOf(NOT_FINISHED)))

        // then
        assertTrue(response.tripPlans.size == 1)
        assertEquals(response.tripPlans.first().passengerId, tripPlanNotFinished.passengerId)
    }

    @Test
    fun `list trip plans by passenger id`() {
        // given
        val passengerId = UUID.randomUUID()
        val tripPlanNotFinished =
            TripPlanFactory.withASingleTripLeg(passengerId = passengerId, status = NOT_FINISHED)
        val tripPlanCancelled = TripPlanFactory.withASingleTripLeg(passengerId = passengerId, status = CANCELLED)
        every { tripPlanRepository.find(commandQuery = match { it.passengerId == passengerId }) } returns listOf(
            tripPlanNotFinished,
            tripPlanCancelled
        )

        // when
        val response =
            listTripPlan(ListTripPlan.Input(passengerId = passengerId))

        // then
        assertTrue(response.tripPlans.size == 2)
        assertTrue(response.tripPlans.map { it.passengerId }.contains(tripPlanNotFinished.passengerId))
        assertTrue(response.tripPlans.map { it.passengerId }.contains(tripPlanCancelled.passengerId))
    }

    @Test
    fun `list trip plans by passenger id and status, but do not match any result`() {
        // given
        val passengerId = UUID.randomUUID()
        val tripPlanNotFinished =
            TripPlanFactory.withASingleTripLeg(passengerId = passengerId, status = FINISHED)
        val tripPlanCancelled = TripPlanFactory.withASingleTripLeg(passengerId = passengerId, status = CANCELLED)
        every { tripPlanRepository.find(commandQuery = match { it.passengerId == passengerId }) } returns listOf(
            tripPlanNotFinished,
            tripPlanCancelled
        )

        // when
        val response =
            listTripPlan(ListTripPlan.Input(passengerId = passengerId, status = listOf(NOT_FINISHED)))

        // then
        assertTrue(response.tripPlans.isEmpty())
    }

}