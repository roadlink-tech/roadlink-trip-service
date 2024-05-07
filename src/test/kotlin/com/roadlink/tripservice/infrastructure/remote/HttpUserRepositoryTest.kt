package com.roadlink.tripservice.infrastructure.remote

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import com.roadlink.tripservice.dev_tools.infrastructure.network.Get
import com.roadlink.tripservice.domain.user.User
import com.roadlink.tripservice.infrastructure.remote.user.HttpUserRepository
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

@MicronautTest
class HttpUserRepositoryTest {

    @Inject
    private lateinit var objectMapper: ObjectMapper

    private lateinit var wireMockServer: WireMockServer

    private lateinit var userRepository: HttpUserRepository

    @BeforeEach
    fun setup() {
        wireMockServer = WireMockServer(wireMockConfig().dynamicPort())
        wireMockServer.start()

        userRepository = HttpUserRepository(
            get = Get(),
            scheme = "http",
            host = "localhost",
            port = wireMockServer.port(),
            endpoint = "user/{userId}",
            objectMapper = objectMapper
        )
        configureFor("localhost", wireMockServer.port())
    }


    @AfterEach
    fun tearDown() {
        wireMockServer.stop()
    }

    @Test
    fun `should return user when user exists`() {
        // given
        val userId = UUID.randomUUID()
        stubFor(
            get(urlEqualTo("/user/$userId"))
                .willReturn(
                    aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                            """
                            {
                                "id": "$userId",
                                "first_name": "John",
                                "last_name": "Doe",
                                "email": "johndoe@example.com",
                                "user_name": "johndoe",
                                "profile_photo_url": "http://example.com/photo.jpg",
                                "gender": "male",
                                "friends": ["e0a778c4-ca85-490a-80a0-979244a7995b"]
                            }
                            """.trimIndent()
                        )
                        .withStatus(200)
                )
        )

        // given
        val user = userRepository.findByUserId(userId.toString())

        // then
        assertNotNull(user)
        assertEquals(userId.toString(), user?.id)
        assertEquals("John", user?.firstName)
        assertEquals("Doe", user?.lastName)
        assertEquals("http://example.com/photo.jpg", user?.profilePhotoUrl)
        assertEquals(setOf(UUID.fromString("e0a778c4-ca85-490a-80a0-979244a7995b")), user?.friendsIds)
        assertEquals(User.Gender.None, user?.gender)
    }

    @Test
    fun `should return null when user does not exist`() {
        // given
        val userId = UUID.randomUUID()
        stubFor(
            get(urlEqualTo("/user/$userId"))
                .willReturn(
                    aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                            """{}""".trimIndent()
                        )
                        .withStatus(404)
                )
        )

        // given
        val user = userRepository.findByUserId(userId.toString())

        // then
        assertNull(user)
    }


}