package com.roadlink.tripservice.infrastructure.remote.trip.feedback_solicitude

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.roadlink.tripservice.dev_tools.infrastructure.network.Post
import com.roadlink.tripservice.dev_tools.infrastructure.network.WriteRequest
import com.roadlink.tripservice.domain.trip.feedback_solicitude.FeedbackSolicitude
import com.roadlink.tripservice.domain.trip.feedback_solicitude.FeedbackSolicitudeRepository
import org.slf4j.LoggerFactory
import java.util.*

class HttpFeedbackSolicitudeRepository(
    private val post: Post,
    private val scheme: String,
    private val host: String,
    private val port: Int,
    private val endpoint: String,
    private val objectMapper: ObjectMapper,
) : FeedbackSolicitudeRepository {
    companion object {
        private val logger = LoggerFactory.getLogger(HttpFeedbackSolicitudeRepository::class.java)
    }

    override fun insert(feedbackSolicitude: FeedbackSolicitude) {
        val httpRequest = WriteRequest.Builder()
            .scheme(scheme)
            .host(host)
            .port(port)
            .body(
                objectMapper.writeValueAsString(
                    CreateFeedbackSolicitudeRequest.from(
                        feedbackSolicitude
                    )
                )
            )
            .endpoint(endpoint.replace("{userId}", feedbackSolicitude.receiverId.toString()))
            .build()

        val response = post.dispatch(httpRequest)

        if (response.isSucceeded()) {
            logger.info("the feedback solicitude $feedbackSolicitude was created successfully")
        } else {
            logger.error("it was an error while creating feedback solicitude $feedbackSolicitude. Error: ${response.body}")
        }
    }
}

data class CreateFeedbackSolicitudeRequest(
    @JsonProperty("reviewer_id")
    val reviewerId: UUID,
    @JsonProperty("trip_leg_id")
    val tripLegId: UUID
) {
    companion object {
        fun from(feedbackSolicitude: FeedbackSolicitude): CreateFeedbackSolicitudeRequest {
            return CreateFeedbackSolicitudeRequest(
                reviewerId = feedbackSolicitude.reviewerId,
                tripLegId = feedbackSolicitude.tripLegId
            )
        }
    }
}