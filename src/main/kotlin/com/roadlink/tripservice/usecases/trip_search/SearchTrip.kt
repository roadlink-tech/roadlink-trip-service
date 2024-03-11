package com.roadlink.tripservice.usecases.trip_search

import com.roadlink.tripservice.domain.common.Location
import com.roadlink.tripservice.domain.trip_search.SearchEngine
import com.roadlink.tripservice.domain.trip_search.TripSearchPlanResult
import java.time.Instant

class SearchTrip(
    private val searchEngine: SearchEngine,
) {

    operator fun invoke(input: Input): List<TripSearchPlanResult> {
        return searchEngine.search(
            departure = input.departure,
            arrival = input.arrival,
            at = input.at,
        )
    }

    data class Input(
        val departure: Location,
        val arrival: Location,
        val at: Instant,
    )
}
