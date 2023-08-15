package com.roadlink.tripservice.trip.infrastructure.rest

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.roadlink.tripservice.infrastructure.persistence.InMemorySectionRepository
import com.roadlink.tripservice.trip.domain.InstantFactory
import com.roadlink.tripservice.trip.domain.LocationFactory
import com.roadlink.tripservice.trip.domain.SectionFactory
import com.roadlink.tripservice.trip.domain.TripFactory
import com.roadlink.tripservice.trip.infrastructure.rest.factories.SearchTripResponseFactory
import com.roadlink.tripservice.trip.infrastructure.rest.responses.SearchTripExpectedResponse
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.uri.UriBuilder
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

@MicronautTest
internal class SearchTripRestControllerTest {
    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Inject
    private lateinit var inMemorySectionRepository: InMemorySectionRepository

    @Inject
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        inMemorySectionRepository.deleteAll()
    }

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
        inMemorySectionRepository.save(SectionFactory.avCabildo4853_virreyDelPino1800(tripId = UUID.fromString(TripFactory.avCabildo_id)))
        inMemorySectionRepository.save(SectionFactory.virreyDelPino1800_avCabildo20(tripId = UUID.fromString(TripFactory.avCabildo_id)))

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