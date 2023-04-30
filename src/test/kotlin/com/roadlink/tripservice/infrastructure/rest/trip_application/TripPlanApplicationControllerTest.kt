package com.roadlink.tripservice.infrastructure.rest.trip_application

import com.fasterxml.jackson.databind.JsonNode
import com.roadlink.tripservice.usecases.UseCase
import com.roadlink.tripservice.usecases.trip_plan.CreateTripPlanApplication
import com.roadlink.tripservice.usecases.trip_plan.CreateTripPlanApplicationOutput
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.uri.UriBuilder
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

@MicronautTest
class TripPlanApplicationControllerTest {

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Inject
    lateinit var createTripPlanApplication: UseCase<TripPlanApplicationDTO, CreateTripPlanApplicationOutput>

    @MockBean(CreateTripPlanApplication::class)
    fun createTripPlanApplication(): UseCase<TripPlanApplicationDTO, CreateTripPlanApplicationOutput> {
        return mockk<UseCase<TripPlanApplicationDTO, CreateTripPlanApplicationOutput>>()
    }

    @Test
    fun `when create a trip plan and all the sections can receive a passenger, then it must be created successfully`() {
        every { createTripPlanApplication.invoke(any()) } returns CreateTripPlanApplicationOutput.TripPlanApplicationCreated(
            UUID.randomUUID()
        )

        // GIVEN
        val request = HttpRequest.POST(
            UriBuilder.of("/trip-service/trip_plan_application").build(), """{
            | "passenger_id": "CABJ_001",
            | "trips": [
            |   {
            |       "trip_id": "CABJ_002",
            |       "section_ids": [ "A", "B"]
            |   }
            | ]
            |}""".trimMargin()
        )

        // WHEN
        val response = client.toBlocking().exchange(request, JsonNode::class.java)

        // THEN
        assertEquals(HttpStatus.CREATED, response.status)

    }
}