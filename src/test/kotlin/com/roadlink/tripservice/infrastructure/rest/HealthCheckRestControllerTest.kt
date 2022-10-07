package com.roadlink.tripservice.infrastructure.rest

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@MicronautTest
internal class HealthCheckRestControllerTest {
    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    fun `health check should return ok`() {
        val request: HttpRequest<Any> = HttpRequest.GET("/health")
        val body = client.toBlocking().retrieve(request)
        assertNotNull(body)
        assertEquals("ok", body)
    }
}
