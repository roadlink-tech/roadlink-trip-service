package com.roadlink.tripservice.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.roadlink.tripservice.dev_tools.infrastructure.network.Get
import com.roadlink.tripservice.domain.RatingRepository
import com.roadlink.tripservice.infrastructure.persistence.FixedRatingRepository
import com.roadlink.tripservice.infrastructure.remote.HttpRatingRepository
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Value
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory

@Factory
class RatingRepositoryConfig {

    companion object {
        private val logger = LoggerFactory.getLogger(RatingRepositoryConfig::class.java)
    }

    @Value("\${http-get-user-trust-score.scheme}")
    private lateinit var getUserTrustScoreScheme: String

    @Value("\${http-get-user-trust-score.host}")
    private lateinit var getUserTrustScoreHost: String

    @Value("\${http-get-user-trust-score.port}")
    private var getUserTrustScorePort: Int? = null

    @Value("\${http-get-user-trust-score.endpoint}")
    private lateinit var getUserTrustScoreEndpoint: String

    @Singleton
    fun ratingRepository(get: Get, objectMapper: ObjectMapper): RatingRepository {
        logger.debug("Initializing http get user trust score with scheme: {}, host: {}, port: {}, endpoint: {}", getUserTrustScoreScheme, getUserTrustScoreHost, getUserTrustScorePort, getUserTrustScoreEndpoint)

        return HttpRatingRepository(
            get = get,
            scheme = getUserTrustScoreScheme,
            host = getUserTrustScoreHost,
            port = getUserTrustScorePort!!,
            endpoint = getUserTrustScoreEndpoint,
            objectMapper = objectMapper,
        )
    }
}