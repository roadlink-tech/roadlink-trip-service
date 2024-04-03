package com.roadlink.tripservice.config.trip

import com.fasterxml.jackson.databind.ObjectMapper
import com.roadlink.tripservice.dev_tools.infrastructure.network.Post
import com.roadlink.tripservice.domain.trip.feedback_solicitude.FeedbackSolicitudeRepository
import com.roadlink.tripservice.infrastructure.remote.trip.feedback_solicitude.HttpFeedbackSolicitudeRepository
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Value
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory

@Factory
class FeedbackSolicitudeRepositoryConfig {
    companion object {
        private val logger = LoggerFactory.getLogger(FeedbackSolicitudeRepositoryConfig::class.java)
    }

    @Value("\${http-post-feedback-solicitude.scheme}")
    private lateinit var createFeedbackSolicitudeByIdScheme: String

    @Value("\${http-post-feedback-solicitude.host}")
    private lateinit var createFeedbackSolicitudeHost: String

    @Value("\${http-post-feedback-solicitude.port}")
    private var createFeedbackSolicitudeIdPort: Int? = null

    @Value("\${http-post-feedback-solicitude.endpoint}")
    private lateinit var createFeedbackSolicitudeEndpoint: String

    @Singleton
    fun feedbackSolicitudeRepository(post: Post, objectMapper: ObjectMapper): FeedbackSolicitudeRepository {
        logger.info("The HttpFeedbackSolicitudeRepository will start with the following parameters: {\"host\":\"$createFeedbackSolicitudeHost\", \"port\":\"$createFeedbackSolicitudeIdPort\"}")
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