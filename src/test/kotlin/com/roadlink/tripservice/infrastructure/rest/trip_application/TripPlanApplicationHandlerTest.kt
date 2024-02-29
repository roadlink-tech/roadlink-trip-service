package com.roadlink.tripservice.infrastructure.rest.trip_application

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.roadlink.tripservice.infrastructure.rest.trip_application.response.TripPlanApplicationCreatedResponse
import com.roadlink.tripservice.trip.infrastructure.rest.End2EndTest
import com.roadlink.tripservice.usecases.UseCase
import com.roadlink.tripservice.usecases.trip_plan.CreateTripPlanApplication
import com.roadlink.tripservice.usecases.trip_plan.CreateTripPlanApplicationInput
import com.roadlink.tripservice.usecases.trip_plan.CreateTripPlanApplicationOutput
import io.micronaut.context.annotation.Primary
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus.*
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.http.uri.UriBuilder
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.*
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

@MicronautTest
class TripPlanApplicationHandlerTest : End2EndTest() {

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Inject
    private lateinit var objectMapper: ObjectMapper

    @Inject
    lateinit var createTripPlanApplication: UseCase<CreateTripPlanApplicationInput, CreateTripPlanApplicationOutput>

    @Primary
    @Singleton
    @Replaces(CreateTripPlanApplication::class)
    fun createTripPlanApplication(): UseCase<CreateTripPlanApplicationInput, CreateTripPlanApplicationOutput> {
        return mockk(relaxed = true)
    }

    @BeforeEach
    fun clear() {
        clearMocks(createTripPlanApplication)
    }

    // TODO agregar validaciones en el payload
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
        thenTheTripPlanWasCreated(response)
        thenTheTripPlanApplicationHasAnId(response)
    }

    @Test
    fun `when one of the section can not receive any passenger, then an error must be retrieved`() {
        // GIVEN
        every { createTripPlanApplication.invoke(any()) } returns CreateTripPlanApplicationOutput.OneOfTheSectionCanNotReceivePassenger(
            message = "The following section could not receive any passenger"
        )

        // WHEN
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
        val response = try {
            client.toBlocking().exchange(request, JsonNode::class.java)
        } catch (e: HttpClientResponseException) {
            e.response
        }

        // THEN
        assertEquals(PRECONDITION_FAILED.code, response.code())
    }

    private fun thenTheTripPlanApplicationHasAnId(response: HttpResponse<JsonNode>) {
        val apiResponse =
            objectMapper.readValue(response.body()!!.toString(), TripPlanApplicationCreatedResponse::class.java)
        assertNotNull(apiResponse)
    }

    private fun thenTheTripPlanWasCreated(response: HttpResponse<JsonNode>) {
        assertEquals(CREATED, response.status)
    }

}