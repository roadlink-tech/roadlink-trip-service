package com.roadlink.tripservice.domain.trip_search

import com.roadlink.tripservice.domain.common.Location

import java.time.Instant

interface SearchEngine {
    fun search(departure: Location, arrival: Location, at: Instant): List<TripPlan>
}
