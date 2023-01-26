package com.roadlink.tripservice.trip.config

import com.roadlink.tripservice.trip.domain.BruteForceSearchEngine
import com.roadlink.tripservice.trip.usecases.SearchTrip
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class SearchTripConfig {
    @Singleton
    fun searchTrip(bruteForceSearchEngine: BruteForceSearchEngine): SearchTrip {
        return SearchTrip(
            searchEngine = bruteForceSearchEngine,
        )
    }
}