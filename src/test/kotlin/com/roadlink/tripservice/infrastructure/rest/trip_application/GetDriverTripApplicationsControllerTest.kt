package com.roadlink.tripservice.infrastructure.rest.trip_application

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.roadlink.tripservice.infrastructure.persistence.trip_application.InMemoryTripPlanApplicationRepository
import com.roadlink.tripservice.trip.domain.SectionFactory
import com.roadlink.tripservice.trip.domain.TripFactory
import com.roadlink.tripservice.trip.domain.TripPlanApplicationFactory
import com.roadlink.tripservice.trip.infrastructure.rest.factories.DriverTripApplicationResponseFactory
import com.roadlink.tripservice.trip.infrastructure.rest.responses.DriverTripApplicationExpectedResponse
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.uri.UriBuilder
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.collections.List

@MicronautTest
class GetDriverTripApplicationsControllerTest {
    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Inject
    private lateinit var objectMapper: ObjectMapper

    @Inject
    private lateinit var inMemoryTripPlanApplicationRepository: InMemoryTripPlanApplicationRepository

    @Test
    fun `can handle driver trip applications request`() {
        val tripId = UUID.fromString(TripFactory.avCabildo_id)
        val section = SectionFactory.avCabildo(tripId = tripId)
        val tripPlanApplicationPendingApproval = TripPlanApplicationFactory.withASingleTripApplicationPendingApproval(
            sections = listOf(section),
            passengerId = "JOHN",
        )
        val tripPlanApplicationRejected = TripPlanApplicationFactory.withASingleTripApplicationRejected(
            sections = listOf(section),
            passengerId = "JENNA",
        )
        val tripPlanApplicationConfirmed = TripPlanApplicationFactory.withASingleTripApplicationConfirmed(
            sections = listOf(section),
            passengerId = "BJNOVAK",
        )
        listOf(
            tripPlanApplicationPendingApproval,
            tripPlanApplicationRejected,
            tripPlanApplicationConfirmed,
        ).forEach { inMemoryTripPlanApplicationRepository.insert(it) }

        val request: HttpRequest<Any> = HttpRequest
            .GET(
                UriBuilder.of("/trip-service/driver-trip-applications")
                    .queryParam("tripId", tripId)
                    .build()
            )

        val response = client.toBlocking().exchange(request, JsonNode::class.java)

        assertOkBody(
            listOf(
                DriverTripApplicationResponseFactory.avCabildoWithASingleTripApplicationPendingApproval(
                    tripApplicationId = tripPlanApplicationPendingApproval.tripApplications.first().id,
                )
            ),
            response
        )
    }

    @Test
    fun `can handle empty driver trip applications request`() {
        val tripId = UUID.fromString(TripFactory.avCabildo_id)

        val request: HttpRequest<Any> = HttpRequest
            .GET(
                UriBuilder.of("/trip-service/driver-trip-applications")
                    .queryParam("tripId", tripId)
                    .build()
            )

        val response = client.toBlocking().exchange(request, JsonNode::class.java)

        assertOkBody(emptyList(), response)
    }

    private fun assertOkBody(
        driverTripApplicationsResponse: List<DriverTripApplicationExpectedResponse>,
        httpResponse: HttpResponse<JsonNode>
    ) {
        assertEquals(
            objectMapper.readTree(objectMapper.writeValueAsString(driverTripApplicationsResponse)),
            httpResponse.body()!!
        )
    }
}