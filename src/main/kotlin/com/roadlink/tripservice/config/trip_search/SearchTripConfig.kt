package com.roadlink.tripservice.config.trip_search

import com.roadlink.tripservice.domain.trip.TripRepository
import com.roadlink.tripservice.domain.trip_search.algorithm.BruteForceSearchEngine
import com.roadlink.tripservice.domain.trip_search.filter.FilterService
import com.roadlink.tripservice.domain.trip_search.filter.SearchFilterService
import com.roadlink.tripservice.domain.user.UserRepository
import com.roadlink.tripservice.usecases.UseCase
import com.roadlink.tripservice.usecases.trip_search.SearchTrip

import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class SearchTripConfig {

    @Singleton
    fun filterService(tripRepository: TripRepository): FilterService {
        return SearchFilterService(tripRepository = tripRepository)
    }

    @Singleton
    fun searchTrip(
        userRepository: UserRepository,
        bruteForceSearchEngine: BruteForceSearchEngine,
        filterService: FilterService
    ): UseCase<SearchTrip.Input, SearchTrip.Output> {
        return SearchTrip(
            userRepository = userRepository,
            searchEngine = bruteForceSearchEngine,
            filterService = filterService
        )
    }
}