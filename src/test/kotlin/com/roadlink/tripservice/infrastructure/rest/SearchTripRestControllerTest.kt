package com.roadlink.tripservice.infrastructure.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.roadlink.tripservice.domain.InstantFactory
import com.roadlink.tripservice.domain.LocationFactory
import com.roadlink.tripservice.domain.SectionFactory
import com.roadlink.tripservice.infrastructure.persistence.InMemorySectionRepository
import io.micronaut.http.HttpRequest
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
            .GET(UriBuilder.of("/trip-service/trips")
                .queryParam("departureLatitude", LocationFactory.avCabildo_4853().latitude)
                .queryParam("departureLongitude", LocationFactory.avCabildo_4853().longitude)
                .queryParam("arrivalLatitude", LocationFactory.avCabildo_20().latitude)
                .queryParam("arrivalLongitude", LocationFactory.avCabildo_20().longitude)
                .queryParam("at", InstantFactory.october15_12hs().toEpochMilli())
                .build())

        val response = client.toBlocking().exchange(request, String::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertEquals(MediaType.APPLICATION_JSON_TYPE, response.contentType.get())
        assertJsonBody(
            """
                {
                    "tripPlans": []
                }
            """.trimIndent(),
            response.body()!!
        )
    }

    @Test
    fun `given exists a trip plan with one meeting point between the given departure and arrival then should return ok status code and the trip plan in response body`() {
        inMemorySectionRepository.save(SectionFactory.avCabildo4853_virreyDelPino1800())
        inMemorySectionRepository.save(SectionFactory.virreyDelPino1800_avCabildo20())

        val request: HttpRequest<Any> = HttpRequest
            .GET(UriBuilder.of("/trip-service/trips")
                .queryParam("departureLatitude", LocationFactory.avCabildo_4853().latitude)
                .queryParam("departureLongitude", LocationFactory.avCabildo_4853().longitude)
                .queryParam("arrivalLatitude", LocationFactory.avCabildo_20().latitude)
                .queryParam("arrivalLongitude", LocationFactory.avCabildo_20().longitude)
                .queryParam("at", InstantFactory.october15_12hs().toEpochMilli())
                .build())

        val response = client.toBlocking().exchange(request, String::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertEquals(MediaType.APPLICATION_JSON_TYPE, response.contentType.get())
        assertJsonBody(
            """
                {
                    "tripPlans": [
                        {
                            "sections": [
                                {
                                    "departure": {
                                        "location": {
                                            "latitude": -34.540412,
                                            "longitude": -58.474732
                                        },
                                        "at": 1665835200000
                                    },
                                    "arrival": {
                                        "location": {
                                            "latitude": -34.562389,
                                            "longitude": -58.445302
                                        },
                                        "at": 1665853200000
                                    }
                                },
                                {
                                    "departure": {
                                        "location": {
                                            "latitude": -34.562389,
                                            "longitude": -58.445302
                                        },
                                        "at": 1665853200000
                                    },
                                    "arrival": {
                                        "location": {
                                            "latitude": -34.574810,
                                            "longitude": -58.435990
                                        },
                                        "at": 1665856800000
                                    }
                                }
                            ]
                        }
                    ]
                }
            """.trimIndent(),
            response.body()!!
        )
    }

    private fun assertJsonBody(expected: String, actual: String) {
        assertEquals(objectMapper.readTree(expected), objectMapper.readTree(actual))
    }
}