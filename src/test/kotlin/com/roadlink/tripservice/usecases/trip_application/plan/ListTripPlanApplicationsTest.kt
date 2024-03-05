package com.roadlink.tripservice.usecases.trip_application.plan

import com.roadlink.tripservice.domain.trip_application.TripPlanApplication
import com.roadlink.tripservice.domain.trip_application.TripPlanApplicationRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class ListTripPlanApplicationsTest {

    @MockK
    lateinit var tripPlanApplicationRepository: TripPlanApplicationRepository

    private lateinit var listTripPlanApplication: ListTripPlanApplications

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        listTripPlanApplication = ListTripPlanApplications(tripPlanApplicationRepository)
    }

    @Test
    fun `when list trip plan applications and filter by status and passenger id, then return the expected amount`() {
        // GIVEN
        val passengerId: UUID = UUID.randomUUID()
        val status = "CONFIRMED"
        val input = ListTripPlanApplications.Input(passengerId = passengerId, status = status)

        every { tripPlanApplicationRepository.findAllByPassengerId(passengerId) } returns listOf(
            TripPlanApplicationFactory.withASingleTripApplication(tripApplicationStatus = TripPlanApplication.TripApplication.Status.CONFIRMED),
            TripPlanApplicationFactory.withASingleTripApplication(tripApplicationStatus = TripPlanApplication.TripApplication.Status.REJECTED),
            TripPlanApplicationFactory.withASingleTripApplication(tripApplicationStatus = TripPlanApplication.TripApplication.Status.PENDING_APPROVAL),
            TripPlanApplicationFactory.withASingleTripApplication(tripApplicationStatus = TripPlanApplication.TripApplication.Status.CONFIRMED)
        )

        // WHEN
        val output = listTripPlanApplication(input)

        // THEN
        Assertions.assertEquals(2, output.tripPlanApplications.size)
        Assertions.assertEquals(
            2,
            output.tripPlanApplications.filter { it.status() == TripPlanApplication.Status.CONFIRMED }.size)
    }

    @Test
    fun `when list trip plan applications and filter bu status and passenger id, but there no is any result, then the response must be empty`() {
        // GIVEN
        val passengerId: UUID = UUID.randomUUID()
        val status = "PENDING_APPROVAL"
        val input = ListTripPlanApplications.Input(passengerId = passengerId, status = status)

        every { tripPlanApplicationRepository.findAllByPassengerId(passengerId) } returns listOf(
            TripPlanApplicationFactory.withASingleTripApplication(tripApplicationStatus = TripPlanApplication.TripApplication.Status.CONFIRMED),
        )

        // WHEN
        val output = listTripPlanApplication(input)

        // THEN
        Assertions.assertTrue(output.tripPlanApplications.isEmpty())
    }

}