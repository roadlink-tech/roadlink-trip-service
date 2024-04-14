package com.roadlink.tripservice.usecases.trip_solicitude

import com.roadlink.tripservice.domain.common.events.SimpleCommandBus
import com.roadlink.tripservice.domain.trip_plan.TripPlan
import com.roadlink.tripservice.domain.trip_plan.events.OnTripLegSolicitudeAcceptedEventCreateTripPlan
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitudeRepository
import com.roadlink.tripservice.usecases.UseCase
import com.roadlink.tripservice.usecases.trip_plan.CreateTripPlan
import com.roadlink.tripservice.usecases.trip_solicitude.plan.TripPlanSolicitudeFactory
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class AcceptTripLegSolicitudeTest {

    @MockK
    lateinit var tripPlanSolicitudeRepository: TripPlanSolicitudeRepository

    @MockK
    lateinit var createTripPlan: UseCase<CreateTripPlan.Input, CreateTripPlan.Output>

    private lateinit var commandBus: SimpleCommandBus

    private lateinit var acceptTripLegSolicitude: AcceptTripLegSolicitude

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        commandBus = SimpleCommandBus()
        commandBus.registerHandler(
            OnTripLegSolicitudeAcceptedEventCreateTripPlan(
                createTripPlan
            )
        )
        acceptTripLegSolicitude = AcceptTripLegSolicitude(tripPlanSolicitudeRepository, commandBus)
    }

    @Test
    fun `when try to accept a trip leg solicitude, but the plan does not exist, then an expected error must be retrieved`() {
        every { tripPlanSolicitudeRepository.find(any()) } returns emptyList()

        // WHEN
        val output = acceptTripLegSolicitude(AcceptTripLegSolicitudeInput(UUID.randomUUID(), UUID.randomUUID()))

        // THEN
        assertInstanceOf(AcceptTripLegSolicitudeOutput.TripPlanSolicitudeNotExists::class.java, output)
    }

    @Test
    fun `when try to accept a trip plan solicitude, but it has been already rejected, then an error must be retrieved`() {
        every { tripPlanSolicitudeRepository.find(any()) } returns listOf(TripPlanSolicitudeFactory.withASingleTripLegSolicitudeRejected())

        // WHEN
        val output = acceptTripLegSolicitude(AcceptTripLegSolicitudeInput(UUID.randomUUID(), UUID.randomUUID()))

        // THEN
        assertInstanceOf(AcceptTripLegSolicitudeOutput.TripLegSolicitudePlanHasBeenRejected::class.java, output)
    }

    @Test
    fun `when accept a trip plan leg solicitude, then an expected response must be retrieved and it must be saved`() {
        // GIVEN
        val tripLegSolicitudeId = UUID.randomUUID()
        val callerId = UUID.randomUUID()
        val passengerId = UUID.randomUUID()
        val vehicleId = UUID.randomUUID()
        every { tripPlanSolicitudeRepository.update(any()) } just runs
        val tripPlanSolicitude = TripPlanSolicitudeFactory.withASingleTripLegSolicitude(
            tripLegSolicitudeId = tripLegSolicitudeId,
            passengerId = passengerId.toString(),
            vehicleId = vehicleId.toString()
        )
        every { tripPlanSolicitudeRepository.find(any()) } returns listOf(
            tripPlanSolicitude
        )
        every { createTripPlan.invoke(any()) } returns CreateTripPlan.Output(TripPlan.from(tripPlanSolicitude))

        // WHEN
        val output = acceptTripLegSolicitude(AcceptTripLegSolicitudeInput(tripLegSolicitudeId, callerId))

        // THEN
        assertInstanceOf(AcceptTripLegSolicitudeOutput.TripLegSolicitudeAccepted::class.java, output)
        verify { tripPlanSolicitudeRepository.update(any()) }
    }

}