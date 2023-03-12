package com.roadlink.tripservice.config

import com.roadlink.tripservice.infrastructure.persistence.InMemorySectionRepository
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class BruteForceSearchEngineConfig {
    @Singleton
    fun bruteForceSearchEngine(inMemorySectionRepository: InMemorySectionRepository): com.roadlink.tripservice.domain.BruteForceSearchEngine {
        return com.roadlink.tripservice.domain.BruteForceSearchEngine(
            sectionRepository = inMemorySectionRepository,
        )
    }
}