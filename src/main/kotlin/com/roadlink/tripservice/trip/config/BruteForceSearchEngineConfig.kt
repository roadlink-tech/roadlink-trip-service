package com.roadlink.tripservice.trip.config

import com.roadlink.tripservice.trip.domain.BruteForceSearchEngine
import com.roadlink.tripservice.trip.infrastructure.persistence.InMemorySectionRepository
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class BruteForceSearchEngineConfig {
    @Singleton
    fun bruteForceSearchEngine(inMemorySectionRepository: InMemorySectionRepository): BruteForceSearchEngine {
        return BruteForceSearchEngine(
            sectionRepository = inMemorySectionRepository,
        )
    }
}