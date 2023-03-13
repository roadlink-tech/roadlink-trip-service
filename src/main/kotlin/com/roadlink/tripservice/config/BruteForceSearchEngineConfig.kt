package com.roadlink.tripservice.config

import com.roadlink.tripservice.domain.trip.section.SectionRepository
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class BruteForceSearchEngineConfig {
    @Singleton
    fun bruteForceSearchEngine(sectionRepository: SectionRepository): com.roadlink.tripservice.domain.BruteForceSearchEngine {
        return com.roadlink.tripservice.domain.BruteForceSearchEngine(
            sectionRepository = sectionRepository,
        )
    }
}