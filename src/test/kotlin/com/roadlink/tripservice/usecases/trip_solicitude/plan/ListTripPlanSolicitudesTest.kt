package com.roadlink.tripservice.usecases.trip_solicitude.plan

import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitudeRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class ListTripPlanSolicitudesTest {

    @MockK
    lateinit var tripPlanSolicitudeRepository: TripPlanSolicitudeRepository

    private lateinit var listTripPlanSolicitude: ListTripPlanSolicitudes

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        listTripPlanSolicitude = ListTripPlanSolicitudes(tripPlanSolicitudeRepository)
    }

    @Test
    fun `when list trip plan applications and filter by status and passenger id, then return the expected amount`() {
        // GIVEN
        val passengerId: UUID = UUID.randomUUID()
        val status = "CONFIRMED"
        val input = ListTripPlanSolicitudes.Input(passengerId = passengerId, status = status)

        every { tripPlanSolicitudeRepository.find(TripPlanSolicitudeRepository.CommandQuery(passengerId = passengerId)) } returns listOf(
            TripPlanSolicitudeFactory.withASingleTripApplication(tripApplicationStatus = TripPlanSolicitude.TripLegSolicitude.Status.CONFIRMED),
            TripPlanSolicitudeFactory.withASingleTripApplication(tripApplicationStatus = TripPlanSolicitude.TripLegSolicitude.Status.REJECTED),
            TripPlanSolicitudeFactory.withASingleTripApplication(tripApplicationStatus = TripPlanSolicitude.TripLegSolicitude.Status.PENDING_APPROVAL),
            TripPlanSolicitudeFactory.withASingleTripApplication(tripApplicationStatus = TripPlanSolicitude.TripLegSolicitude.Status.CONFIRMED)
        )

        // WHEN
        val output = listTripPlanSolicitude(input)

        // THEN
        Assertions.assertEquals(2, output.tripPlanSolicitudes.size)
        Assertions.assertEquals(
            2,
            output.tripPlanSolicitudes.filter { it.status() == TripPlanSolicitude.Status.CONFIRMED }.size
        )
    }

    @Test
    fun `when list trip plan applications and filter bu status and passenger id, but there no is any result, then the response must be empty`() {
        // GIVEN
        val passengerId: UUID = UUID.randomUUID()
        val status = "PENDING_APPROVAL"
        val input = ListTripPlanSolicitudes.Input(passengerId = passengerId, status = status)

        every { tripPlanSolicitudeRepository.find(TripPlanSolicitudeRepository.CommandQuery(passengerId = passengerId)) } returns listOf(
            TripPlanSolicitudeFactory.withASingleTripApplication(tripApplicationStatus = TripPlanSolicitude.TripLegSolicitude.Status.CONFIRMED),
        )

        // WHEN
        val output = listTripPlanSolicitude(input)

        // THEN
        Assertions.assertTrue(output.tripPlanSolicitudes.isEmpty())
    }

}