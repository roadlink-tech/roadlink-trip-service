package com.roadlink.tripservice.infrastructure.rest.trip_solicitude.handler

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.roadlink.tripservice.infrastructure.rest.trip_solicitude.response.TripPlanSolicitudeCreatedResponse
import com.roadlink.tripservice.infrastructure.End2EndTest
import com.roadlink.tripservice.usecases.UseCase
import com.roadlink.tripservice.usecases.trip_solicitude.plan.*
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
class TripPlanSolicitudeHandlerTest : End2EndTest() {

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Inject
    private lateinit var objectMapper: ObjectMapper

    @Inject
    lateinit var createTripPlanSolicitude: UseCase<CreateTripPlanSolicitude.Input, CreateTripPlanSolicitude.Output>

    @Primary
    @Singleton
    @Replaces(CreateTripPlanSolicitude::class)
    fun createTripPlanSolicitude(): UseCase<CreateTripPlanSolicitude.Input, CreateTripPlanSolicitude.Output> {
        return mockk(relaxed = true)
    }

    @BeforeEach
    fun clear() {
        clearMocks(createTripPlanSolicitude)
    }

    // TODO agregar validaciones en el payload
    /**
     * Create Trip
     */
    @Test
    fun `when create a trip plan solicitude and all the sections can receive a passenger, then it must be created successfully`() {
        every { createTripPlanSolicitude.invoke(any()) } returns CreateTripPlanSolicitude.Output.TripPlanSolicitudeCreated(
            UUID.randomUUID()
        )

        // GIVEN
        val request = HttpRequest.POST(
            UriBuilder.of("/trip-service/trip_plan_solicitudes").build(), """{
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
        thenTheTripPlanSolicitudeHasAnId(response)
    }

    @Test
    fun `when one of the trip leg can not receive any passenger, then an error must be retrieved`() {
        // GIVEN
        every { createTripPlanSolicitude.invoke(any()) } returns CreateTripPlanSolicitude.Output.OneOfTheSectionCanNotReceivePassenger(
            message = "The following section could not receive any passenger"
        )

        // WHEN
        val request = HttpRequest.POST(
            UriBuilder.of("/trip-service/trip_plan_solicitudes").build(), """{
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


    private fun thenTheTripPlanSolicitudeHasAnId(response: HttpResponse<JsonNode>) {
        val apiResponse =
            objectMapper.readValue(response.body()!!.toString(), TripPlanSolicitudeCreatedResponse::class.java)
        assertNotNull(apiResponse)
    }

    private fun thenTheTripPlanWasCreated(response: HttpResponse<JsonNode>) {
        assertEquals(CREATED, response.status)
    }

}