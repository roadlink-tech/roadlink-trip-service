package com.roadlink.tripservice.usecases.trip

import com.roadlink.tripservice.domain.Location
import com.roadlink.tripservice.domain.searcher.SearchEngine
import com.roadlink.tripservice.domain.trip.TripPlan
import java.time.Instant

class SearchTrip(
    private val searchEngine: SearchEngine,
) {

    operator fun invoke(input: Input): List<TripPlan> {
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
