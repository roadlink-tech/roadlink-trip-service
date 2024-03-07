package com.roadlink.tripservice.usecases.trip_solicitude

import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitudeRepository
import com.roadlink.tripservice.usecases.trip_solicitude.plan.TripPlanApplicationFactory
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class AcceptTripLegSolicitudeTest {

    @MockK
    lateinit var tripPlanSolicitudeRepository: TripPlanSolicitudeRepository

    private lateinit var acceptTripLegSolicitude: AcceptTripLegSolicitude

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        acceptTripLegSolicitude = AcceptTripLegSolicitude(tripPlanSolicitudeRepository)
    }

    @Test
    fun `when try to accept a trip application but the plan does not exit, then an expected error must be retrieved`() {
        every { tripPlanSolicitudeRepository.find(any()) } returns emptyList()

        // WHEN
        val output = acceptTripLegSolicitude(AcceptTripLegSolicitudeInput(UUID.randomUUID(), UUID.randomUUID()))

        // THEN
        assertInstanceOf(AcceptTripLegSolicitudeOutput.TripPlanSolicitudeNotExists::class.java, output)
    }

    @Test
    fun `when try to accept a trip plan but the it has been already rejected, then an error response must be retrieved`() {
        every { tripPlanSolicitudeRepository.find(any()) } returns listOf(TripPlanApplicationFactory.withASingleTripApplicationRejected())

        // WHEN
        val output = acceptTripLegSolicitude(AcceptTripLegSolicitudeInput(UUID.randomUUID(), UUID.randomUUID()))

        // THEN
        assertInstanceOf(AcceptTripLegSolicitudeOutput.TripLegSolicitudePlanHasBeenRejected::class.java, output)
    }

    @Test
    fun `when accept a trip plan application, then an expected response must be retrieved and it must be saved`() {
        // GIVEN
        val tripApplicationId = UUID.randomUUID()
        val callerId = UUID.randomUUID()
        every { tripPlanSolicitudeRepository.update(any()) } just runs
        every { tripPlanSolicitudeRepository.find(any()) } returns listOf(
            TripPlanApplicationFactory.withASingleTripApplication(
                tripApplicationId = tripApplicationId
            )
        )

        // WHEN
        val output = acceptTripLegSolicitude(AcceptTripLegSolicitudeInput(tripApplicationId, callerId))

        // THEN
        assertInstanceOf(AcceptTripLegSolicitudeOutput.TripLegSolicitudeAccepted::class.java, output)
        verify { tripPlanSolicitudeRepository.update(any()) }
    }

}