package com.roadlink.tripservice.config.trip

import com.fasterxml.jackson.databind.ObjectMapper
import com.roadlink.tripservice.dev_tools.infrastructure.network.Post
import com.roadlink.tripservice.domain.trip.feedback_solicitude.FeedbackSolicitudeRepository
import com.roadlink.tripservice.infrastructure.remote.trip.feedback_solicitude.HttpFeedbackSolicitudeRepository
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Value
import jakarta.inject.Singleton

@Factory
class FeedbackSolicitudeRepositoryConfig {

    @Value("\${http-post-feedback-solicitude.scheme}")
    private lateinit var createFeedbackSolicitudeByIdScheme: String

    @Value("\${http-post-feedback-solicitude.host}")
    private lateinit var createFeedbackSolicitudeHost: String

    @Value("\${http-post-feedback-solicitude.port}")
    private var createFeedbackSolicitudeIdPort: Int? = null

    @Value("\${http-post-feedback-solicitude.endpoint}")
    private lateinit var createFeedbackSolicitudeEndpoint: String

    @Singleton
    fun feedbackSolicitudeRepository(
        post: Post, objectMapper: ObjectMapper
    ): FeedbackSolicitudeRepository {
        return HttpFeedbackSolicitudeRepository(
            post = post,
            scheme = createFeedbackSolicitudeByIdScheme,
            host = createFeedbackSolicitudeHost,
            port = createFeedbackSolicitudeIdPort!!,
            endpoint = createFeedbackSolicitudeEndpoint,
            objectMapper = objectMapper
        )
    }
}