package com.roadlink.tripservice.domain

import com.roadlink.tripservice.domain.trip.TripPlan
import java.time.Instant

interface SearchEngine {
    fun search(departure: Location, arrival: Location, at: Instant): List<TripPlan>
}
