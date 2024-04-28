package com.roadlink.tripservice.infrastructure.rest.trip_search

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.roadlink.tripservice.domain.common.Location
import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.infrastructure.End2EndTest
import com.roadlink.tripservice.infrastructure.factories.SearchTripResponseFactory
import com.roadlink.tripservice.infrastructure.rest.trip_search.response.SearchTripResponse
import com.roadlink.tripservice.usecases.common.address.LocationFactory
import com.roadlink.tripservice.usecases.common.InstantFactory
import com.roadlink.tripservice.usecases.trip.SectionFactory
import com.roadlink.tripservice.usecases.trip.TripFactory
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
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
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
        sectionRepository.save(
            SectionFactory.avCabildo4853_virreyDelPino1800(
                tripId = UUID.fromString(
                    TripFactory.avCabildo_id
                )
            )
        )
        sectionRepository.save(
            SectionFactory.virreyDelPino1800_avCabildo20(
                tripId = UUID.fromString(
                    TripFactory.avCabildo_id
                )
            )
        )
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
        assertOkBody(
            SearchTripResponseFactory.avCabildo4853_virreyDelPino1800_avCabildo20(),
            response
        )
    }

    @Test
    fun `given an existing trip plan, when search by a near arrival and departure location place by a radius of 1 percent of the total distance, then a result must be retrieved`() {
        val tripId = UUID.fromString(
            TripFactory.avCabildo_id
        )
        sectionRepository.save(
            SectionFactory.avCabildo4853_virreyDelPino1800(
                tripId = tripId
            )
        )
        sectionRepository.save(
            SectionFactory.virreyDelPino1800_avCabildo20(
                tripId = tripId
            )
        )
        entityManager.transaction.commit()

        val departureLocation = Location(
            latitude = -34.540412,
            longitude = -58.474362,
            alias = "Location 40 mt to the east of AvCabildo 4853"
        )

        val arrivalLocation = Location(
            latitude = -34.574810,
            longitude = -58.436030,
            alias = "Location 40 mt to the west of AvCabildo 20"
        )

        val request: HttpRequest<Any> = HttpRequest
            .GET(
                UriBuilder.of("/trip-service/trips")
                    .queryParam("departureLatitude", departureLocation.latitude)
                    .queryParam("departureLongitude", departureLocation.longitude)
                    .queryParam("arrivalLatitude", arrivalLocation.latitude)
                    .queryParam("arrivalLongitude", arrivalLocation.longitude)
                    .queryParam("at", InstantFactory.october15_12hs().toEpochMilli())
                    .build()
            )

        val response = client.toBlocking().exchange(request, JsonNode::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertEquals(MediaType.APPLICATION_JSON_TYPE, response.contentType.get())
        assertOkBody(
            SearchTripResponseFactory.avCabildo4853_virreyDelPino1800_avCabildo20(),
            response
        )
    }

    @Test
    fun `given an existing trip plan, when search by an arrival location place by a radius greater than 1 percent of the total distance, then none result must be retrieved`() {
        val tripId = UUID.fromString(
            TripFactory.avCabildo_id
        )
        sectionRepository.save(
            SectionFactory.avCabildo4853_virreyDelPino1800(
                tripId = tripId
            )
        )
        sectionRepository.save(
            SectionFactory.virreyDelPino1800_avCabildo20(
                tripId = tripId
            )
        )
        entityManager.transaction.commit()

        val departureLocation = Location(
            latitude = -34.550,
            longitude = -58.474732,
            alias = "Location 1 km to the south AvCabildo 4853"
        )


        val arrivalLocation = Location(
            latitude = -34.574810,
            longitude = -58.436030,
            alias = "Location 40 mt to the west of AvCabildo 20"
        )

        val request: HttpRequest<Any> = HttpRequest
            .GET(
                UriBuilder.of("/trip-service/trips")
                    .queryParam("departureLatitude", departureLocation.latitude)
                    .queryParam("departureLongitude", departureLocation.longitude)
                    .queryParam("arrivalLatitude", arrivalLocation.latitude)
                    .queryParam("arrivalLongitude", arrivalLocation.longitude)
                    .queryParam("at", InstantFactory.october15_12hs().toEpochMilli())
                    .build()
            )

        val response = client.toBlocking().exchange(request, JsonNode::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertEquals(MediaType.APPLICATION_JSON_TYPE, response.contentType.get())
        assertOkBody(
            SearchTripResponseFactory.empty(),
            response
        )
    }

    @Test
    fun `given an existing trip plan, when search by a departure location place by a radius greater than 1 percent of the total distance, then none result must be retrieved`() {
        val tripId = UUID.fromString(
            TripFactory.avCabildo_id
        )
        sectionRepository.save(
            SectionFactory.avCabildo4853_virreyDelPino1800(
                tripId = tripId
            )
        )
        sectionRepository.save(
            SectionFactory.virreyDelPino1800_avCabildo20(
                tripId = tripId
            )
        )
        entityManager.transaction.commit()

        val departureLocation = Location(
            latitude = -34.540412,
            longitude = -58.474362,
            alias = "Location 40 mt to the east of AvCabildo 4853"
        )

        val arrivalLocation = Location(
            latitude = -34.584,
            longitude = -58.435990,
            alias = "Location 1 km to the south of AvCabildo 20"
        )


        val request: HttpRequest<Any> = HttpRequest
            .GET(
                UriBuilder.of("/trip-service/trips")
                    .queryParam("departureLatitude", departureLocation.latitude)
                    .queryParam("departureLongitude", departureLocation.longitude)
                    .queryParam("arrivalLatitude", arrivalLocation.latitude)
                    .queryParam("arrivalLongitude", arrivalLocation.longitude)
                    .queryParam("at", InstantFactory.october15_12hs().toEpochMilli())
                    .build()
            )

        val response = client.toBlocking().exchange(request, JsonNode::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertEquals(MediaType.APPLICATION_JSON_TYPE, response.contentType.get())
        assertOkBody(
            SearchTripResponseFactory.empty(),
            response
        )
    }

    @Test
    fun `given an existing trip plan, when search by a departure location place by a radius less than 15 km of the total distance, then a result must be retrieved`() {
        val tripId = UUID.fromString(
            TripFactory.avCabildo_id
        )
        sectionRepository.save(
            SectionFactory.caba_ushuaia(
                tripId = tripId
            )
        )
        entityManager.transaction.commit()

        val departureLocation =
            Location(latitude = -34.6497, longitude = -58.3815, alias = "Location 14 km from CABA")

        val arrivalLocation = LocationFactory.ushuaia()


        val request: HttpRequest<Any> = HttpRequest
            .GET(
                UriBuilder.of("/trip-service/trips")
                    .queryParam("departureLatitude", departureLocation.latitude)
                    .queryParam("departureLongitude", departureLocation.longitude)
                    .queryParam("arrivalLatitude", arrivalLocation.latitude)
                    .queryParam("arrivalLongitude", arrivalLocation.longitude)
                    .queryParam("at", InstantFactory.october15_12hs().toEpochMilli())
                    .build()
            )

        val response = client.toBlocking().exchange(request, JsonNode::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertEquals(MediaType.APPLICATION_JSON_TYPE, response.contentType.get())
        assertFalse(response.body().isEmpty)
        // TODO check the payload response
    }

    @Test
    fun `given an existing trip plan, when search by a departure and arrival location place by a radius less than 15 km of the total distance, then a result must be retrieved`() {
        val tripId = UUID.fromString(
            TripFactory.avCabildo_id
        )
        sectionRepository.save(
            SectionFactory.caba_ushuaia(
                tripId = tripId
            )
        )
        entityManager.transaction.commit()

        val departureLocation =
            Location(latitude = -34.6497, longitude = -58.3815, alias = "Location 14 km from CABA")

        val arrivalLocation = Location(
            latitude = -54.7706,
            longitude = -68.3022,
            alias = "Location 10 km from Ushuaia"
        )

        val request: HttpRequest<Any> = HttpRequest
            .GET(
                UriBuilder.of("/trip-service/trips")
                    .queryParam("departureLatitude", departureLocation.latitude)
                    .queryParam("departureLongitude", departureLocation.longitude)
                    .queryParam("arrivalLatitude", arrivalLocation.latitude)
                    .queryParam("arrivalLongitude", arrivalLocation.longitude)
                    .queryParam("at", InstantFactory.october15_12hs().toEpochMilli())
                    .build()
            )

        val response = client.toBlocking().exchange(request, JsonNode::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertEquals(MediaType.APPLICATION_JSON_TYPE, response.contentType.get())
        assertFalse(response.body().isEmpty)
        // TODO check the payload response
    }

    @Test
    fun `given an existing trip plan, when search by a departure location place by a radius greater than 15 km of the total distance, then none result must be retrieved`() {
        val tripId = UUID.fromString(
            TripFactory.avCabildo_id
        )
        sectionRepository.save(
            SectionFactory.caba_ushuaia(
                tripId = tripId
            )
        )
        entityManager.transaction.commit()

        val departureLocation = Location(
            latitude = -34.2536,
            longitude = -58.8647,
            alias = "Ubicación a 50 km de 9 de Julio"
        )

        val arrivalLocation = Location(
            latitude = -54.7706,
            longitude = -68.3022,
            alias = "Location 10 km from Ushuaia"
        )

        val request: HttpRequest<Any> = HttpRequest
            .GET(
                UriBuilder.of("/trip-service/trips")
                    .queryParam("departureLatitude", departureLocation.latitude)
                    .queryParam("departureLongitude", departureLocation.longitude)
                    .queryParam("arrivalLatitude", arrivalLocation.latitude)
                    .queryParam("arrivalLongitude", arrivalLocation.longitude)
                    .queryParam("at", InstantFactory.october15_12hs().toEpochMilli())
                    .build()
            )

        val response = client.toBlocking().exchange(request, JsonNode::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertEquals(MediaType.APPLICATION_JSON_TYPE, response.contentType.get())
        assertOkBody(
            SearchTripResponseFactory.empty(),
            response
        )
    }

    private fun assertOkBody(
        searchTripResponse: SearchTripResponse,
        httpResponse: HttpResponse<JsonNode>
    ) {
        assertEquals(
            objectMapper.readTree(objectMapper.writeValueAsString(searchTripResponse)),
            httpResponse.body()!!
        )
    }

}

