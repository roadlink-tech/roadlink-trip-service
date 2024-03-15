package com.roadlink.tripservice.infrastructure.remote

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.roadlink.tripservice.dev_tools.infrastructure.network.Get
import com.roadlink.tripservice.domain.user.UserTrustScore
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

@MicronautTest
class HttpUserTrustScoreRepositoryTest {

    @Inject
    private lateinit var objectMapper: ObjectMapper

    private lateinit var wireMockServer: WireMockServer

    private lateinit var userTrustScoreRepository: HttpUserTrustScoreRepository

    @BeforeEach
    fun setup() {
        wireMockServer = WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort())
        wireMockServer.start()

        userTrustScoreRepository = HttpUserTrustScoreRepository(
            get = Get(),
            scheme = "http",
            host = "localhost",
            port = wireMockServer.port(),
            endpoint = "users/{userId}/user_trust_score",
            objectMapper = objectMapper
        )
        WireMock.configureFor("localhost", wireMockServer.port())
    }


    @AfterEach
    fun tearDown() {
        wireMockServer.stop()
    }

    @Test
    fun `should return user when user exists`() {
        // given
        val userId = UUID.randomUUID()
        WireMock.stubFor(
            WireMock.get(WireMock.urlEqualTo("/users/$userId/user_trust_score"))
                .willReturn(
                    WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                            """
                            {
                                "score": 4.5,
                                "feedbacks": {
                                    "given": 10,
                                    "received": 20
                                },
                                "enrollment_days": 365,
                                "friends": 5
                            }
                            """.trimIndent()
                        )
                        .withStatus(200)
                )
        )

        // given
        val user = userTrustScoreRepository.findById(userId.toString())

        // then
        assertNotNull(user)
        assertEquals(4.5, user.score)
        assertEquals(UserTrustScore.Feedbacks(given = 10, received = 20), user.feedbacks)
    }


}