package com.roadlink.tripservice.usecases.trip_plan

import com.roadlink.tripservice.domain.trip_application.TripPlanApplicationRepository
import com.roadlink.tripservice.trip.domain.TripPlanApplicationFactory
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class AcceptTripApplicationTest {

    @MockK
    lateinit var tripPlanApplicationRepository: TripPlanApplicationRepository

    private lateinit var acceptTripApplication: AcceptTripApplication

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        acceptTripApplication = AcceptTripApplication(tripPlanApplicationRepository)
    }

    @Test
    fun `when try to accept a trip application but the plan does not exit, then an expected error must be retrieved`() {
        every { tripPlanApplicationRepository.findByTripApplicationId(any()) } returns null

        // WHEN
        val output = acceptTripApplication(UUID.randomUUID())

        // THEN
        assertInstanceOf(AcceptTripApplicationOutput.TripPlanApplicationNotExists::class.java, output)
    }

    @Test
    fun `when try to accept a trip plan but the it has been already rejected, then an error response must be retrieved`() {
        every { tripPlanApplicationRepository.findByTripApplicationId(any()) } returns TripPlanApplicationFactory.withAnApplicationRejected()

        // WHEN
        val output = acceptTripApplication(UUID.randomUUID())

        // THEN
        assertInstanceOf(AcceptTripApplicationOutput.TripApplicationPlanHasBeenRejected::class.java, output)
    }

    @Test
    fun `when accept a trip plan application, then an expected response must be retrieved and it must be saved`() {
        // GIVEN
        val tripPlanApplicationId = UUID.randomUUID()
        every { tripPlanApplicationRepository.save(any()) } just runs
        every { tripPlanApplicationRepository.findByTripApplicationId(any()) } returns TripPlanApplicationFactory.withASingleTripApplication(
            tripPlanApplicationId
        )

        // WHEN
        val output = acceptTripApplication(tripPlanApplicationId)

        // THEN
        assertInstanceOf(AcceptTripApplicationOutput.TripApplicationAccepted::class.java, output)
        verify { tripPlanApplicationRepository.save(any()) }
    }

}