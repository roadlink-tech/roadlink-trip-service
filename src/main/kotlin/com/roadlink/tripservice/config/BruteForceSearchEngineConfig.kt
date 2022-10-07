package com.roadlink.tripservice.config

import com.roadlink.tripservice.domain.BruteForceSearchEngine
import com.roadlink.tripservice.infrastructure.repositories.InMemorySectionRepository
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