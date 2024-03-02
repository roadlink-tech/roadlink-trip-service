package com.roadlink.tripservice.config.trip_search

import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.domain.trip_search.BruteForceSearchEngine
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class BruteForceSearchEngineConfig {
    @Singleton
    fun bruteForceSearchEngine(sectionRepository: SectionRepository): BruteForceSearchEngine {
        return BruteForceSearchEngine(
            sectionRepository = sectionRepository,
        )
    }
}