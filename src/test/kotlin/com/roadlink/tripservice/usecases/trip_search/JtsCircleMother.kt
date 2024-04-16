package com.roadlink.tripservice.usecases.trip_search

import com.roadlink.tripservice.domain.common.Location
import com.roadlink.tripservice.domain.trip_search.JtsSearchCircleCreator
import org.locationtech.jts.geom.Polygon

object JtsCircleMother {
    private val jtsSearchCircleCreator = JtsSearchCircleCreator()

    fun common(location: Location, radiusInMeters: Double = 100.0): Polygon {
        return jtsSearchCircleCreator.from(location, radiusInMeters).value
    }
}