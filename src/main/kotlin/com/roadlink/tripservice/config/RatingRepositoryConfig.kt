package com.roadlink.tripservice.config

import com.roadlink.tripservice.domain.RatingRepository
import com.roadlink.tripservice.infrastructure.persistence.FixedRatingRepository
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class RatingRepositoryConfig {
    @Singleton
    fun ratingRepository(): RatingRepository {
        return FixedRatingRepository()
    }
}