package com.roadlink.tripservice.infrastructure.rest.driver_trip

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.roadlink.tripservice.domain.trip.Trip
import com.roadlink.tripservice.domain.trip.TripRepository
import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitudeRepository
import com.roadlink.tripservice.domain.user.UserRepository
import com.roadlink.tripservice.domain.user.UserTrustScore
import com.roadlink.tripservice.domain.user.UserTrustScoreRepository
import com.roadlink.tripservice.infrastructure.End2EndTest
import com.roadlink.tripservice.infrastructure.factories.DriverTripDetailResponseFactory
import com.roadlink.tripservice.infrastructure.remote.user.HttpUserRepository
import com.roadlink.tripservice.infrastructure.remote.user.HttpUserTrustScoreRepository
import com.roadlink.tripservice.infrastructure.rest.driver_trip.response.DriverTripDetailResponse
import com.roadlink.tripservice.usecases.trip.SectionFactory
import com.roadlink.tripservice.usecases.trip.TripFactory
import com.roadlink.tripservice.usecases.trip_solicitude.plan.TripPlanSolicitudeFactory
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

@MicronautTest
class RetrieveDriverTripDetailsHandlerE2ETest : End2EndTest() {
    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Inject
    private lateinit var objectMapper: ObjectMapper

    @Inject
    private lateinit var sectionRepository: SectionRepository

    @Inject
    private lateinit var tripRepository: TripRepository

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
    fun `can handle driver trip details request`() {
        // given
        val section = SectionFactory.avCabildo(
            initialAmountOfSeats = 4,
            bookedSeats = 1,
        )
        val userId = UUID.randomUUID()
        sectionRepository.save(section)

        TripFactory.avCabildo4853_virreyDelPino1800_avCabildo20(
            id = section.tripId.toString(),
            status = Trip.Status.FINISHED
        ).also { tripRepository.insert(it) }

        listOf(
            TripPlanSolicitudeFactory.withASingleTripLegSolicitudeAccepted(
                sections = listOf(SectionFactory.avCabildo()),
                passengerId = userId.toString(),
            ),
        ).forEach { tripPlanSolicitudeRepository.insert(it) }

        entityManager.transaction.commit()

        every { userRepository.findByUserId(any()) } returns UserFactory.common(
            id = userId,
            firstName = "Painn",
            lastName = "Wilson"
        )
        every { userTrustScoreRepository.findById(any()) } returns UserTrustScoreFactory.common(
            score = 0.0,
            feedbacks = UserTrustScore.Feedbacks(given = 0, received = 0)
        )

        val request: HttpRequest<Any> = HttpRequest
            .GET(
                UriBuilder.of("/trip-service/driver-trip-detail")
                    .queryParam("tripId", section.tripId)
                    .build()
            )

        // when
        val response = client.toBlocking().exchange(request, JsonNode::class.java)

        // then
        assertOkBody(
            DriverTripDetailResponseFactory.avCabildoWithASingleTripApplicationConfirmed(
                tripId = section.tripId.toString(),
                userId = userId,
                profilePhotoUrl = "http//profile.photo.com"
            ),
            response
        )
    }

    private fun assertOkBody(
        driverTripDetailResponse: DriverTripDetailResponse,
        httpResponse: HttpResponse<JsonNode>
    ) {
        assertEquals(
            objectMapper.readTree(objectMapper.writeValueAsString(driverTripDetailResponse)),
            httpResponse.body()!!
        )
    }
}