package com.roadlink.tripservice.trip.infrastructure.rest

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.domain.trip_application.TripPlanApplicationRepository
import com.roadlink.tripservice.trip.domain.SectionFactory
import com.roadlink.tripservice.trip.domain.TripPlanApplicationFactory
import com.roadlink.tripservice.trip.infrastructure.rest.factories.DriverTripDetailResponseFactory
import com.roadlink.tripservice.trip.infrastructure.rest.responses.DriverTripDetailExpectedResponse
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.uri.UriBuilder
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@MicronautTest
class RetrieveDriverTripDetailsHandlerTest : End2EndTest() {
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
    fun `can handle driver trip details request`() {
        val section = SectionFactory.avCabildo(
            initialAmountOfSeats = 4,
            bookedSeats = 1,
        )
        sectionRepository.save(section)
        listOf(
            TripPlanApplicationFactory.withASingleTripApplicationConfirmed(
                sections = listOf(SectionFactory.avCabildo()),
                passengerId = "PAINN",
            ),
        ).forEach { tripPlanApplicationRepository.insert(it) }

        entityManager.transaction.commit()

        val request: HttpRequest<Any> = HttpRequest
            .GET(
                UriBuilder.of("/trip-service/driver-trip-detail")
                    .queryParam("tripId", section.tripId)
                    .build()
            )

        val response = client.toBlocking().exchange(request, JsonNode::class.java)

        assertOkBody(
            DriverTripDetailResponseFactory.avCabildoWithASingleTripApplicationConfirmed(tripId = section.tripId.toString()),
            response
        )
    }

    private fun assertOkBody(
        driverTripDetailResponse: DriverTripDetailExpectedResponse,
        httpResponse: HttpResponse<JsonNode>
    ) {
        assertEquals(
            objectMapper.readTree(objectMapper.writeValueAsString(driverTripDetailResponse)),
            httpResponse.body()!!
        )
    }
}