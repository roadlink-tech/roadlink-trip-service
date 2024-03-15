package com.roadlink.tripservice.infrastructure.factories

import com.roadlink.tripservice.infrastructure.rest.trip.request.LocationRequest

object LocationRequestFactory {
    fun avCabildo_4853() =
        LocationRequest(
            latitude = -34.540412,
            longitude = -58.474732,
        )

    fun avCabildo_20() =
        LocationRequest(
            latitude = -34.574810,
            longitude = -58.435990,
        )

    fun virreyDelPino_1800() =
        LocationRequest(
            latitude = -34.562389,
            longitude = -58.445302,
        )
}
