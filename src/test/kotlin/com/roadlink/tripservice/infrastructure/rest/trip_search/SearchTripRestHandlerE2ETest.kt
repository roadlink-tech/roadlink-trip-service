package com.roadlink.tripservice.infrastructure.rest.trip_search

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.usecases.trip.domain.InstantFactory
import com.roadlink.tripservice.usecases.trip.domain.LocationFactory
import com.roadlink.tripservice.usecases.trip.domain.SectionFactory
import com.roadlink.tripservice.usecases.trip.domain.TripFactory
import com.roadlink.tripservice.infrastructure.End2EndTest
import com.roadlink.tripservice.infrastructure.factories.SearchTripResponseFactory
import com.roadlink.tripservice.infrastructure.rest.responses.SearchTripExpectedResponse
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.uri.UriBuilder
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

@MicronautTest
internal class SearchTripRestHandlerE2ETest : End2EndTest() {
    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Inject
    private lateinit var sectionRepository: SectionRepository

    @Inject
    private lateinit var objectMapper: ObjectMapper

    @Inject
    private lateinit var entityManager: EntityManager

    @Test
    fun `given no section exists then should return ok status code and the trip plan in response body`() {
        val request: HttpRequest<Any> = HttpRequest
            .GET(
                UriBuilder.of("/trip-service/trips")
                    .queryParam("departureLatitude", LocationFactory.avCabildo_4853().latitude)
                    .queryParam("departureLongitude", LocationFactory.avCabildo_4853().longitude)
                    .queryParam("arrivalLatitude", LocationFactory.avCabildo_20().latitude)
                    .queryParam("arrivalLongitude", LocationFactory.avCabildo_20().longitude)
                    .queryParam("at", InstantFactory.october15_12hs().toEpochMilli())
                    .build()
            )

        val response = client.toBlocking().exchange(request, JsonNode::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertEquals(MediaType.APPLICATION_JSON_TYPE, response.contentType.get())
        assertOkBody(SearchTripResponseFactory.empty(), response)
    }

    @Test
    fun `given exists a trip plan with one meeting point between the given departure and arrival then should return ok status code and the trip plan in response body`() {
        sectionRepository.save(SectionFactory.avCabildo4853_virreyDelPino1800(tripId = UUID.fromString(TripFactory.avCabildo_id)))
        sectionRepository.save(SectionFactory.virreyDelPino1800_avCabildo20(tripId = UUID.fromString(TripFactory.avCabildo_id)))
        entityManager.transaction.commit()

        val request: HttpRequest<Any> = HttpRequest
            .GET(
                UriBuilder.of("/trip-service/trips")
                    .queryParam("departureLatitude", LocationFactory.avCabildo_4853().latitude)
                    .queryParam("departureLongitude", LocationFactory.avCabildo_4853().longitude)
                    .queryParam("arrivalLatitude", LocationFactory.avCabildo_20().latitude)
                    .queryParam("arrivalLongitude", LocationFactory.avCabildo_20().longitude)
                    .queryParam("at", InstantFactory.october15_12hs().toEpochMilli())
                    .build()
            )

        val response = client.toBlocking().exchange(request, JsonNode::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertEquals(MediaType.APPLICATION_JSON_TYPE, response.contentType.get())
        assertOkBody(SearchTripResponseFactory.avCabildo4853_virreyDelPino1800_avCabildo20(), response)
    }

    private fun assertOkBody(searchTripResponse: SearchTripExpectedResponse, httpResponse: HttpResponse<JsonNode>) {
        assertEquals(
            objectMapper.readTree(objectMapper.writeValueAsString(searchTripResponse)),
            httpResponse.body()!!
        )
    }

}

