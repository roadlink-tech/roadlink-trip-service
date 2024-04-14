package com.roadlink.tripservice.infrastructure.rest.trip_solicitude.handler

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitudeRepository
import com.roadlink.tripservice.domain.user.UserRepository
import com.roadlink.tripservice.domain.user.UserTrustScoreRepository
import com.roadlink.tripservice.usecases.trip.SectionFactory
import com.roadlink.tripservice.usecases.trip.TripFactory
import com.roadlink.tripservice.usecases.trip_solicitude.plan.TripPlanSolicitudeFactory
import com.roadlink.tripservice.infrastructure.End2EndTest
import com.roadlink.tripservice.infrastructure.factories.DriverTripApplicationResponseFactory
import com.roadlink.tripservice.infrastructure.remote.user.HttpUserRepository
import com.roadlink.tripservice.infrastructure.remote.user.HttpUserTrustScoreRepository
import com.roadlink.tripservice.infrastructure.rest.driver_trip.response.DriverTripLegSolicitudeResponse
import com.roadlink.tripservice.usecases.user.UserFactory
import com.roadlink.tripservice.usecases.user.UserTrustScoreFactory
import io.micronaut.context.annotation.Primary
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.uri.UriBuilder
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.every
import io.mockk.mockk
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.collections.List

@MicronautTest
class ListDriverTripLegSolicitudesHandlerE2ETest : End2EndTest() {

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Inject
    private lateinit var objectMapper: ObjectMapper

    @Inject
    private lateinit var sectionRepository: SectionRepository

    @Inject
    private lateinit var tripPlanSolicitudeRepository: TripPlanSolicitudeRepository

    @Inject
    private lateinit var userTrustScoreRepository: UserTrustScoreRepository

    @Inject
    private lateinit var userRepository: UserRepository

    @Primary
    @Singleton
    @Replaces(HttpUserRepository::class)
    fun userRepository(): HttpUserRepository {
        return mockk(relaxed = true)
    }

    @Primary
    @Singleton
    @Replaces(UserTrustScoreRepository::class)
    fun userTrustScoreRepository(): HttpUserTrustScoreRepository {
        return mockk(relaxed = true)
    }

    @Test
    fun `can handle driver trip applications request`() {
        val tripId = UUID.fromString(TripFactory.avCabildo_id)
        val section = SectionFactory.avCabildo(tripId = tripId)
        sectionRepository.save(section)

        val tripPlanSolicitudePendingApproval = TripPlanSolicitudeFactory.withASingleTripLegSolicitudePendingApproval(
            sections = listOf(section),
            passengerId = "JOHN",
        )
        val tripPlanSolicitudeRejected = TripPlanSolicitudeFactory.withASingleTripLegSolicitudeRejected(
            sections = listOf(section),
            passengerId = "JENNA",
        )
        val tripPlanSolicitudeConfirmed = TripPlanSolicitudeFactory.withASingleTripLegSolicitudeAccepted(
            sections = listOf(section),
            passengerId = "BJNOVAK",
        )
        listOf(
            tripPlanSolicitudePendingApproval,
            tripPlanSolicitudeRejected,
            tripPlanSolicitudeConfirmed,
        ).forEach { tripPlanSolicitudeRepository.insert(it) }

        entityManager.transaction.commit()

        val userId = UUID.randomUUID()
        every { userRepository.findByUserId(any()) } returns UserFactory.common(id = userId)
        every { userTrustScoreRepository.findById(any()) } returns UserTrustScoreFactory.common(score = 1.3)
        val request: HttpRequest<Any> = HttpRequest
            .GET(
                UriBuilder.of("/trip-service/driver_trip_leg_solicitudes")
                    .queryParam("tripId", tripId)
                    .build()
            )

        // when
        val response = client.toBlocking().exchange(request, JsonNode::class.java)

        // then
        assertOkBody(
            listOf(
                DriverTripApplicationResponseFactory.avCabildoWithASingleTripApplicationPendingApproval(
                    tripApplicationId = tripPlanSolicitudePendingApproval.tripLegSolicitudes.first().id,
                    userId = userId,
                    profilePhotoUrl = "http//profile.photo.com"
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
                UriBuilder.of("/trip-service/driver_trip_leg_solicitudes")
                    .queryParam("tripId", tripId)
                    .build()
            )

        val response = client.toBlocking().exchange(request, JsonNode::class.java)

        assertOkBody(emptyList(), response)
    }

    private fun assertOkBody(
        driverTripApplicationsResponse: List<DriverTripLegSolicitudeResponse>,
        httpResponse: HttpResponse<JsonNode>
    ) {
        assertEquals(
            objectMapper.readTree(objectMapper.writeValueAsString(driverTripApplicationsResponse)),
            httpResponse.body()!!
        )
    }
}