package com.roadlink.tripservice.infrastructure.rest.mappers

import com.roadlink.tripservice.domain.Location
import com.roadlink.tripservice.infrastructure.rest.responses.LocationResponse

object LocationResponseMapper {
    fun map(location: Location) =
        LocationResponse(
            latitude = location.latitude,
            longitude = location.longitude,
        )
}