package com.roadlink.tripservice.usecases.trip_plan

import com.roadlink.tripservice.domain.trip_application.TripPlanApplicationRepository
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

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
    fun `when try to accept a trip but the plan does not exit, then an expected response must be retrieved`() {
        //TODO
    }

}