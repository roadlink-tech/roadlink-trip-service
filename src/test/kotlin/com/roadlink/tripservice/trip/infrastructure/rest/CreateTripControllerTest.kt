package com.roadlink.tripservice.trip.infrastructure.rest

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.roadlink.tripservice.domain.trip.Trip
import com.roadlink.tripservice.infrastructure.persistence.InMemoryTripRepository
import com.roadlink.tripservice.trip.StubIdGenerator
import com.roadlink.tripservice.trip.domain.TripFactory
import com.roadlink.tripservice.trip.infrastructure.rest.factories.CreateTripRequestFactory
import com.roadlink.tripservice.trip.infrastructure.rest.factories.TripResponseFactory
import com.roadlink.tripservice.trip.infrastructure.rest.requests.CreateTripExpectedRequest
import com.roadlink.tripservice.trip.infrastructure.rest.responses.TripExpectedResponse
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.uri.UriBuilder
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@MicronautTest
class CreateTripControllerTest {
    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Inject
    private lateinit var objectMapper: ObjectMapper

    @Inject
    private lateinit var stubIdGenerator: StubIdGenerator

    @Inject
    private lateinit var inMemoryTripRepository: InMemoryTripRepository

    @BeforeEach
    fun beforeEach() {
        inMemoryTripRepository.deleteAll()
    }

    @Test
    fun `can create trip with no meeting points`() {
        stubIdGenerator.nextIdToGenerate(id = TripFactory.avCabildoId)
        val request = request(CreateTripRequestFactory.avCabildo())

        val response = client.toBlocking().exchange(request, JsonNode::class.java)

        assertEquals(HttpStatus.CREATED, response.status)
        assertEquals(MediaType.APPLICATION_JSON_TYPE, response.contentType.get())
        assertOkBody(TripResponseFactory.avCabildo(), response)
        thenTripExists(TripFactory.avCabildo())
    }

    @Test
    fun `can create trip with meeting points`() {
        stubIdGenerator.nextIdToGenerate(id = TripFactory.avCabildo4853_virreyDelPino1800_avCabildo20)
        val request = request(CreateTripRequestFactory.avCabildo4853_virreyDelPino1800_avCabildo20())

        val response = client.toBlocking().exchange(request, JsonNode::class.java)

        assertEquals(HttpStatus.CREATED, response.status)
        assertEquals(MediaType.APPLICATION_JSON_TYPE, response.contentType.get())
        assertOkBody(TripResponseFactory.avCabildo4853_virreyDelPino1800_avCabildo20(), response)
        thenTripExists(TripFactory.avCabildo4853_virreyDelPino1800_avCabildo20())
    }

    private fun request(createTripRequest: CreateTripExpectedRequest): HttpRequest<String> {
        val body = objectMapper.writeValueAsString(createTripRequest)
        return HttpRequest.POST(UriBuilder.of("/trip-service/trip").build(), body)
    }

    private fun assertOkBody(tripResponse: TripExpectedResponse, httpResponse: HttpResponse<JsonNode>) {
        assertEquals(
            objectMapper.readTree(objectMapper.writeValueAsString(tripResponse)),
            httpResponse.body()!!
        )
    }

    private fun thenTripExists(trip: Trip) {
        assertEquals(listOf(trip), inMemoryTripRepository.findAll())
    }
}