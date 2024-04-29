package com.roadlink.tripservice.domain.trip_search

import com.roadlink.tripservice.domain.common.Location
import kotlin.math.*

object DistanceOnEarthInMeters {

    private const val radiusOfEarthInMeters = 6371000.0

    operator fun invoke(location1: Location, location2: Location): Double {
        val (latitude1, longitude1) = location1
        val (latitude2, longitude2) = location2

        val latitude1InRadians = Math.toRadians(latitude1)
        val longitude1InRadians = Math.toRadians(longitude1)
        val latitude2InRadians = Math.toRadians(latitude2)
        val longitude2InRadians = Math.toRadians(longitude2)

        // Haversine formula
        val dlon: Double = longitude2InRadians - longitude1InRadians
        val dlat: Double = latitude2InRadians - latitude1InRadians
        val a: Double = sin(dlat / 2)
            .pow(2.0) + cos(latitude1InRadians) * cos(latitude2InRadians) * sin(dlon / 2).pow(2.0)

        val c = 2 * asin(sqrt(a))

        // calculate the result
        return (c * radiusOfEarthInMeters)
    }
}