package com.roadlink.tripservice.usecases

import com.roadlink.tripservice.domain.Location
import com.roadlink.tripservice.domain.SearchEngine
import com.roadlink.tripservice.domain.TripPlan
import java.time.Instant

class SearchTrip(
    private val searchEngine: SearchEngine,
) {

    operator fun invoke(request: Request): List<TripPlan> {
        return searchEngine.search(
            departure = request.departure,
            arrival = request.arrival,
            at = request.at,
        )
    }

    data class Request(
        val departure: Location,
        val arrival: Location,
        val at: Instant,
    )
}
