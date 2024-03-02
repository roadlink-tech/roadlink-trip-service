package com.roadlink.tripservice.infrastructure.rest.trip

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.roadlink.tripservice.domain.time.TimeRange
import com.roadlink.tripservice.domain.trip.Trip
import com.roadlink.tripservice.domain.trip.TripRepository
import com.roadlink.tripservice.infrastructure.End2EndTest
import com.roadlink.tripservice.usecases.trip.SpyCommandBus
import com.roadlink.tripservice.usecases.trip.StubIdGenerator
import com.roadlink.tripservice.usecases.trip.domain.TripFactory
import com.roadlink.tripservice.infrastructure.factories.AlreadyExistsTripByDriverInTimeRangeResponseFactory
import com.roadlink.tripservice.infrastructure.factories.CreateTripRequestFactory
import com.roadlink.tripservice.infrastructure.factories.InvalidTripTimeRangeResponseFactory
import com.roadlink.tripservice.infrastructure.factories.TripResponseFactory
import com.roadlink.tripservice.infrastructure.requests.CreateTripExpectedRequest
import com.roadlink.tripservice.infrastructure.rest.responses.TripExpectedResponse
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus.*
import io.micronaut.http.MediaType.*
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.http.uri.UriBuilder
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

@MicronautTest
class CreateTripHandlerE2ETest : End2EndTest() {
    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Inject
    private lateinit var objectMapper: ObjectMapper

    @Inject
    private lateinit var stubIdGenerator: StubIdGenerator

    @Inject
    private lateinit var tripRepository: TripRepository

    @Inject
    private lateinit var spyCommandBus: SpyCommandBus

    @Inject
    private lateinit var entityManager: EntityManager

    @BeforeEach
    fun beforeEach() {
        spyCommandBus.clear()
    }

    @Test
    fun `can create trip with no meeting points`() {
        stubIdGenerator.nextIdToGenerate(id = TripFactory.avCabildo_id)
        val request = request(CreateTripRequestFactory.avCabildo())

        val response = client.toBlocking().exchange(request, JsonNode::class.java)

        assertEquals(CREATED, response.status)
        assertEquals(APPLICATION_JSON_TYPE, response.contentType.get())
        assertOkBody(TripResponseFactory.avCabildo(), response)
        thenTripExists(TripFactory.avCabildo4853_to_avCabildo20())
        theCommandHasBeenPublished()
    }

    @Test
    fun `can create trip with meeting points`() {
        stubIdGenerator.nextIdToGenerate(id = TripFactory.avCabildo4853_virreyDelPino1800_avCabildo20_id)
        val request = request(CreateTripRequestFactory.avCabildo4853_virreyDelPino1800_avCabildo20())

        val response = client.toBlocking().exchange(request, JsonNode::class.java)

        assertEquals(CREATED, response.status)
        assertEquals(APPLICATION_JSON_TYPE, response.contentType.get())
        assertOkBody(TripResponseFactory.avCabildo4853_virreyDelPino1800_avCabildo20(), response)
        thenTripExists(TripFactory.avCabildo4853_virreyDelPino1800_avCabildo20())
        theCommandHasBeenPublished()
    }

    @Test
    fun `given already exists trip with same driver in the given time range then should fail`() {
        tripRepository.save(TripFactory.avCabildo4853_to_avCabildo20())
        entityManager.transaction.commit()
        val request = request(CreateTripRequestFactory.avCabildo())

        val response = try {
            client.toBlocking().exchange(request, Argument.of(JsonNode::class.java), Argument.of(JsonNode::class.java))
        } catch (e: HttpClientResponseException) {
            e.response
        }

        assertEquals(CONFLICT, response.status)
        assertEquals(APPLICATION_JSON_TYPE, response.contentType.get())
        assertErrorBody(AlreadyExistsTripByDriverInTimeRangeResponseFactory.avCabildo(), response)
        theCommandHasNotBeenPublished()
    }

    @Test
    fun `given trip with no meeting points and arrival at before departure at then should fail`() {
        val request = request(CreateTripRequestFactory.avCabildo_invalidTimeRange())

        val response = try {
            client.toBlocking().exchange(request, Argument.of(JsonNode::class.java), Argument.of(JsonNode::class.java))
        } catch (e: HttpClientResponseException) {
            e.response
        }

        assertEquals(BAD_REQUEST, response.status)
        assertEquals(APPLICATION_JSON_TYPE, response.contentType.get())
        assertErrorBody(InvalidTripTimeRangeResponseFactory.avCabildo_invalidTimeRange(), response)
        theCommandHasNotBeenPublished()
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

    private fun <T> assertErrorBody(errorResponse: T, httpResponse: HttpResponse<out Any>) {
        assertEquals(
            objectMapper.readTree(objectMapper.writeValueAsString(errorResponse)),
            httpResponse.getBody(JsonNode::class.java).get()
        )
    }

    private fun thenTripExists(trip: Trip) {
        assertTrue { tripRepository.existsByDriverAndInTimeRange(
            driver = trip.driverId,
            timeRange = TimeRange(trip.departure.estimatedArrivalTime, trip.arrival.estimatedArrivalTime))
        }
    }

    private fun theCommandHasBeenPublished() {
        assertFalse(spyCommandBus.publishedCommands.isEmpty())
    }

    private fun theCommandHasNotBeenPublished() {
        assertTrue(spyCommandBus.publishedCommands.isEmpty())
    }

}