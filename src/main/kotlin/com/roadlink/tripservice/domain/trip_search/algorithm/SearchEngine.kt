package com.roadlink.tripservice.domain.trip_search.algorithm

import com.roadlink.tripservice.domain.common.Location
import com.roadlink.tripservice.domain.trip_search.TripSearchPlanResult

import java.time.Instant

interface SearchEngine {
    fun search(departure: Location, arrival: Location, at: Instant): List<TripSearchPlanResult>
}
