package com.roadlink.tripservice.usecases.trip_plan

import com.roadlink.tripservice.domain.trip_plan.TripPlanRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class RetrieveTripPlanTest {

    @MockK
    lateinit var tripPlanRepository: TripPlanRepository

    private lateinit var retrieveTripPlan: RetrieveTripPlan

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        retrieveTripPlan = RetrieveTripPlan(tripPlanRepository)
    }

    @Test
    fun `retrieve trip plan by id`() {
        // given
        val tripPlan = TripPlanFactory.common()
        every { tripPlanRepository.find(commandQuery = match { it.id == tripPlan.id }) } returns listOf(
            tripPlan,
        )

        // when
        val output = retrieveTripPlan(RetrieveTripPlan.Input(tripPlanId = tripPlan.id))

        // then
        assertTrue(output is RetrieveTripPlan.Output.TripPlanFound)
        assertEquals((output as RetrieveTripPlan.Output.TripPlanFound).tripPlan, tripPlan)
    }

    @Test
    fun `retrieve trip plan by id, but do not match any result`() {
        // given
        val tripPlanId = UUID.randomUUID()
        every { tripPlanRepository.find(commandQuery = match { it.id == tripPlanId }) } returns emptyList()

        // when
        val output = retrieveTripPlan(RetrieveTripPlan.Input(tripPlanId = tripPlanId))

        // then
        assertTrue(output is RetrieveTripPlan.Output.TripPlanNotFound)
    }

}