package com.roadlink.tripservice.infrastructure.remote

import com.fasterxml.jackson.databind.ObjectMapper
import com.roadlink.tripservice.dev_tools.infrastructure.network.Get
import com.roadlink.tripservice.dev_tools.infrastructure.network.ReadRequest
import com.roadlink.tripservice.domain.user.UserRepository

class HttpUserRepository(
    private val get: Get,
    private val scheme: String,
    private val host: String,
    private val port: Int,
    private val endpoint: String,
    private val objectMapper: ObjectMapper,
) : UserRepository {
    override fun findFullNameById(userId: String): String? {
        val httpRequest = ReadRequest.Builder()
            .scheme(scheme)
            .host(host)
            .port(port)
            .endpoint(endpoint.replace("{userId}", userId))
            .build()

        val response = get.dispatch(httpRequest)

        return when {
            response.isSucceeded() -> {
                val userResponse = objectMapper.readTree(response.body)
                val firstName = userResponse.get("first_name").asText()
                val lastName = userResponse.get("last_name").asText()
                "$firstName $lastName"
            }
            response.statusCode == 404 -> {
                null
            }
            else -> {
                throw RuntimeException("Unknown error")
            }
        }
    }
}
