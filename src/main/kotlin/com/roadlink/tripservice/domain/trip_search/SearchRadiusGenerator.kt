package com.roadlink.tripservice.domain.trip_search

import com.roadlink.tripservice.domain.common.Location

/**
 * SearchRadiusGenerator calculates and returns the search radius for a given departure and arrival location.
 */
object SearchRadiusGenerator {


    // Maximum allowable radius for the search area, representing a tolerance of up to 15 kilometers.
    private const val maxCircleSearchAreaRadius = 15_000.0

    // Percentage of the total distance to be used as the search radius.
    private const val percentageOfTotalDistance = 0.01

    operator fun invoke(departure: Location, arrival: Location): Double {
        val radiusInMeters = DistanceOnEarthInMeters(departure, arrival) * percentageOfTotalDistance
        return if (radiusInMeters >= maxCircleSearchAreaRadius)
            maxCircleSearchAreaRadius
        else
            radiusInMeters
    }


}