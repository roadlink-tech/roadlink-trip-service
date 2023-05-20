package com.roadlink.tripservice.infrastructure.rest.trip_application

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.roadlink.tripservice.usecases.UseCase
import com.roadlink.tripservice.usecases.trip_plan.*
import io.micronaut.context.annotation.Primary
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.http.uri.UriBuilder
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

@MicronautTest
class TripApplicationControllerTest {

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Inject
    private lateinit var objectMapper: ObjectMapper

    @Inject
    lateinit var rejectTripApplication: UseCase<UUID, RejectTripApplicationOutput>

    @Primary
    @Singleton
    @Replaces(RejectTripApplication::class)
    fun rejectTripApplication(): UseCase<UUID, RejectTripApplicationOutput> {
        return mockk(relaxed = true)
    }

    @BeforeEach
    fun clear() {
        clearMocks(rejectTripApplication)
    }

    @Test
    fun `when try to reject which not exist, then an error must be retrieved`() {
        every { rejectTripApplication.invoke(any()) } returns RejectTripApplicationOutput.TripPlanApplicationNotExists

        // GIVEN
        val request =
            HttpRequest.PUT(
                UriBuilder.of(
                    "/trip-service/trip_application/${
                        UUID.randomUUID()
                    }/non-acceptance"
                ).build(), """"""
            )

        val response = try {
            client.toBlocking().exchange(request, JsonNode::class.java)
        } catch (e: HttpClientResponseException) {
            e.response
        }

        // THEN
        assertEquals(HttpStatus.NOT_FOUND.code, response.code())
    }

    @Test
    fun `when an application was rejected successfully then a 200 response must be retrieved`() {
        every { rejectTripApplication.invoke(any()) } returns RejectTripApplicationOutput.TripApplicationRejected

        // GIVEN
        val request =
            HttpRequest.PUT(
                UriBuilder.of(
                    "/trip-service/trip_application/${
                        UUID.randomUUID()
                    }/non-acceptance"
                ).build(), """"""
            )

        // WHEN
        val response = client.toBlocking().exchange(request, JsonNode::class.java)

        // THEN
        assertEquals(HttpStatus.ACCEPTED.code, response.code())
    }
}