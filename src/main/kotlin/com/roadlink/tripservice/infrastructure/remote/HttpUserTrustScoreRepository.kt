package com.roadlink.tripservice.infrastructure.remote

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.roadlink.tripservice.dev_tools.infrastructure.network.Get
import com.roadlink.tripservice.dev_tools.infrastructure.network.ReadRequest
import com.roadlink.tripservice.domain.user.UserError
import com.roadlink.tripservice.domain.user.UserTrustScore
import com.roadlink.tripservice.domain.user.UserTrustScoreRepository

class HttpUserTrustScoreRepository(
    private val get: Get,
    private val scheme: String,
    private val host: String,
    private val port: Int,
    private val endpoint: String,
    private val objectMapper: ObjectMapper,
) : UserTrustScoreRepository {
    override fun findById(id: String): UserTrustScore {
        val httpRequest = ReadRequest.Builder()
            .scheme(scheme)
            .host(host)
            .port(port)
            .endpoint(endpoint.replace("{userId}", id))
            .build()

        val response = get.dispatch(httpRequest)

        return when {
            response.isSucceeded() -> {
                val userTrustScoreResponse =
                    objectMapper.readValue(response.body, UserTrustScoreCoreResponse::class.java)
                userTrustScoreResponse.toDomain()
            }

            response.statusCode == 404 -> {
                throw UserError.NotExists(id)
            }

            else -> {
                throw RuntimeException("Unknown error")
            }
        }
    }
}

data class UserTrustScoreCoreResponse(
    @JsonProperty("score")
    val score: Double,
    @JsonProperty("feedbacks")
    val feedbacks: FeedbacksCoreResponse,
    @JsonProperty("enrollment_days")
    val enrollmentDays: Long,
    @JsonProperty("friends")
    val friends: Int
) {
    data class FeedbacksCoreResponse(
        @JsonProperty("given")
        val given: Int,
        @JsonProperty("received")
        val received: Int
    ) {
        fun toModel(): UserTrustScore.Feedbacks {
            return UserTrustScore.Feedbacks(
                given = given,
                received = received
            )
        }
    }

    fun toDomain(): UserTrustScore {
        return UserTrustScore(
            score = score,
            feedbacks = feedbacks.toModel()
        )
    }
}
