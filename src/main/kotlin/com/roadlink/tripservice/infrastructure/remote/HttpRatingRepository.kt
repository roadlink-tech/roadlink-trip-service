package com.roadlink.tripservice.infrastructure.remote

import com.fasterxml.jackson.databind.ObjectMapper
import com.roadlink.tripservice.dev_tools.infrastructure.network.Get
import com.roadlink.tripservice.dev_tools.infrastructure.network.ReadRequest
import com.roadlink.tripservice.domain.RatingRepository

class HttpRatingRepository(
    private val get: Get,
    private val scheme: String,
    private val host: String,
    private val port: Int,
    private val endpoint: String,
    private val objectMapper: ObjectMapper,
) : RatingRepository {
    override fun findByUserId(userId: String): Double? {
        val httpRequest = ReadRequest.Builder()
            .scheme(scheme)
            .host(host)
            .port(port)
            .endpoint(endpoint.replace("{userId}", userId))
            .build()

        val response = get.dispatch(httpRequest)

        return when {
            response.isSucceeded() -> {
                val userTrustScoreResponse = objectMapper.readTree(response.body)
                val feedbacksReceived = userTrustScoreResponse.get("feedbacks").get("received").asInt()
                if (feedbacksReceived == 0) {
                    null
                } else {
                    userTrustScoreResponse.get("score").asDouble()
                }
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