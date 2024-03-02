package com.roadlink.tripservice.config.trip_search

import com.roadlink.tripservice.domain.trip_search.BruteForceSearchEngine
import com.roadlink.tripservice.usecases.trip_search.SearchTrip

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