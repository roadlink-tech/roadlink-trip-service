package com.roadlink.tripservice.infrastructure.rest.trip_solicitude.handler

import com.fasterxml.jackson.databind.JsonNode
import com.roadlink.tripservice.infrastructure.End2EndTest
import com.roadlink.tripservice.usecases.UseCase
import com.roadlink.tripservice.usecases.trip_solicitude.AcceptTripLegSolicitude
import com.roadlink.tripservice.usecases.trip_solicitude.AcceptTripLegSolicitudeOutput
import com.roadlink.tripservice.usecases.trip_solicitude.RejectTripLegSolicitude
import com.roadlink.tripservice.usecases.trip_solicitude.RejectTripLegSolicitudeOutput
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
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.util.*

@MicronautTest
class TripLegSolicitudeHandlerTest : End2EndTest() {

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Inject
    lateinit var rejectTripApplication: UseCase<UUID, RejectTripLegSolicitudeOutput>

    @Inject
    lateinit var acceptTripApplication: UseCase<UUID, AcceptTripLegSolicitudeOutput>

    @Primary
    @Singleton
    @Replaces(RejectTripLegSolicitude::class)
    fun rejectTripApplication(): UseCase<UUID, RejectTripLegSolicitudeOutput> {
        return mockk(relaxed = true)
    }

    @Primary
    @Singleton
    @Replaces(AcceptTripLegSolicitude::class)
    fun acceptTripApplication(): UseCase<UUID, AcceptTripLegSolicitudeOutput> {
        return mockk(relaxed = true)
    }

    @BeforeEach
    fun clear() {
        clearMocks(rejectTripApplication)
        clearMocks(acceptTripApplication)
    }

    @Test
    fun `when try to reject which not exist, then an error must be retrieved`() {
        every { rejectTripApplication.invoke(any()) } returns RejectTripLegSolicitudeOutput.TripPlanLegSolicitudeNotExists

        // GIVEN
        val request =
            HttpRequest.PUT(
                UriBuilder.of(
                    "/trip-service/trip_solicitude/${
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
        every { rejectTripApplication.invoke(any()) } returns RejectTripLegSolicitudeOutput.TripLegSolicitudeRejected

        // GIVEN
        val request =
            HttpRequest.PUT(
                UriBuilder.of(
                    "/trip-service/trip_leg_solicitudes/${
                        UUID.randomUUID()
                    }/reject"
                ).build(), """"""
            )

        // WHEN
        val response = client.toBlocking().exchange(request, JsonNode::class.java)

        // THEN
        assertEquals(HttpStatus.ACCEPTED.code, response.code())
    }

    @Test
    fun `when try to accept an application but it does not exist, then a not found must be retrieved`() {
        val callerId = UUID.randomUUID()
        every { acceptTripApplication.invoke(any()) } returns AcceptTripLegSolicitudeOutput.TripPlanSolicitudeNotExists

        // GIVEN
        val request =
            HttpRequest.PUT(
                UriBuilder.of(
                    "/trip-service/trip_solicitude/${
                        UUID.randomUUID()
                    }/acceptance"
                ).build(), """"""
            ).header("X-Caller-Id", callerId.toString())

        val response = try {
            client.toBlocking().exchange(request, JsonNode::class.java)
        } catch (e: HttpClientResponseException) {
            e.response
        }

        // THEN
        assertEquals(HttpStatus.NOT_FOUND.code, response.code())
    }

    @Disabled
    @Test
    // TODO fix me!
    fun `when try to accept an solicitude but the plan has been rejected by someone else, then an error must be retrieved`() {
        val callerId = UUID.randomUUID()
        every { acceptTripApplication.invoke(any()) } returns AcceptTripLegSolicitudeOutput.TripLegSolicitudePlanHasBeenRejected

        // GIVEN
        val request =
            HttpRequest.PUT(
                UriBuilder.of(
                    "/trip-service/trip_solicitude/${
                        UUID.randomUUID()
                    }/accept"
                ).build(), """"""
            ).header("X-Caller-Id", callerId.toString())

        val response = try {
            client.toBlocking().exchange(request, JsonNode::class.java)
        } catch (e: HttpClientResponseException) {
            e.response
        }

        // THEN
        assertEquals(HttpStatus.PRECONDITION_FAILED.code, response.code())
    }
}