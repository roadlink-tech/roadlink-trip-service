package com.roadlink.tripservice.config.trip_search

import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.domain.trip_search.*
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class BruteForceSearchEngineConfig {

    @Singleton
    fun circleSearchAreaCreator(): SearchAreaCreator<JtsCircle> {
        return JtsSearchCircleCreator()
    }

    @Singleton
    fun distanceOnEarthInMeters(): DistanceOnEarthInMeters {
        return DistanceOnEarthInMeters()
    }

    @Singleton
    fun bruteForceSearchEngine(
        sectionRepository: SectionRepository,
        circleSearchAreaCreator: SearchAreaCreator<JtsCircle>,
        distanceOnEarthInMeters: DistanceOnEarthInMeters,
    ): BruteForceSearchEngine {
        return BruteForceSearchEngine(
            sectionRepository = sectionRepository,
            circleSearchAreaCreator = circleSearchAreaCreator,
            distanceOnEarthInMeters = distanceOnEarthInMeters,
        )
    }
}