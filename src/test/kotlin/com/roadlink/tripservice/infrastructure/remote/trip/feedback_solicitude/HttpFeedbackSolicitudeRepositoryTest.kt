package com.roadlink.tripservice.infrastructure.remote.trip.feedback_solicitude

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.roadlink.tripservice.dev_tools.infrastructure.network.Post
import com.roadlink.tripservice.domain.trip.feedback_solicitude.FeedbackSolicitude
import com.roadlink.tripservice.domain.trip.feedback_solicitude.FeedbackSolicitudeRepository
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

@MicronautTest
class HttpFeedbackSolicitudeRepositoryTest {

    @Inject
    private lateinit var objectMapper: ObjectMapper

    private lateinit var wireMockServer: WireMockServer

    private lateinit var repository: FeedbackSolicitudeRepository

    @BeforeEach
    fun setup() {
        wireMockServer = WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort())
        wireMockServer.start()

        repository = HttpFeedbackSolicitudeRepository(
            post = Post(),
            scheme = "http",
            host = "localhost",
            port = wireMockServer.port(),
            endpoint = "users/{userId}/feedback_solicitudes",
            objectMapper = objectMapper
        )
        WireMock.configureFor("localhost", wireMockServer.port())
    }

    @Test
    fun `should not fail when create a feedback solicitude`() {
        // given
        val receiverId = UUID.randomUUID()
        val reviewerId = UUID.randomUUID()
        val tripLegId = UUID.randomUUID()

        wireMockServer.stubFor(
            post(urlEqualTo("/users/$receiverId/feedback_solicitudes"))
                .withRequestBody(
                    equalToJson(
                        "{ \"reviewer_id\": \"$reviewerId\", \"trip_leg_id\": \"$tripLegId\" }",
                        true,
                        true
                    )
                )
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                            """{
                                "id":"${UUID.randomUUID()}",
                                "reviewer_id":"$reviewerId",
                                "receiver_id":"$receiverId",
                                "trip_leg_id":"$tripLegId",
                                "status":"PENDING"
                            }""".trimIndent().replace(Regex("\\s+"), "")
                        )
                )
        )

        // when
        repository.insert(
            FeedbackSolicitude(
                reviewerId = reviewerId,
                receiverId = receiverId,
                tripLegId = tripLegId
            )
        )

        // then

    }
}