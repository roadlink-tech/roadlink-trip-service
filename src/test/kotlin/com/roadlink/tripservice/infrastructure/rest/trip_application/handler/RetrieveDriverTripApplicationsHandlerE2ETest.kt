package com.roadlink.tripservice.infrastructure.rest.trip_application.handler

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.domain.trip_application.TripPlanApplicationRepository
import com.roadlink.tripservice.usecases.factory.SectionFactory
import com.roadlink.tripservice.usecases.trip.TripFactory
import com.roadlink.tripservice.usecases.trip_application.plan.TripPlanApplicationFactory
import com.roadlink.tripservice.infrastructure.End2EndTest
import com.roadlink.tripservice.infrastructure.factories.DriverTripApplicationResponseFactory
import com.roadlink.tripservice.infrastructure.rest.responses.DriverTripApplicationExpectedResponse
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.uri.UriBuilder
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.collections.List

@MicronautTest
class RetrieveDriverTripApplicationsHandlerE2ETest : End2EndTest() {
    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Inject
    private lateinit var objectMapper: ObjectMapper

    @Inject
    private lateinit var sectionRepository: SectionRepository

    @Inject
    private lateinit var tripPlanApplicationRepository: TripPlanApplicationRepository

    @Inject
    private lateinit var entityManager: EntityManager

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
        sectionRepository.save(section)
        listOf(
            tripPlanApplicationPendingApproval,
            tripPlanApplicationRejected,
            tripPlanApplicationConfirmed,
        ).forEach { tripPlanApplicationRepository.insert(it) }

        entityManager.transaction.commit()

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