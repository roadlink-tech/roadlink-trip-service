package com.fdt.tripservice.usecases

import com.fdt.tripservice.domain.*
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
