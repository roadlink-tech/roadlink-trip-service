package com.roadlink.tripservice.trip.infrastructure.rest.factories

import com.roadlink.tripservice.trip.infrastructure.rest.requests.LocationExpectedRequest

object LocationRequestFactory {
    fun avCabildo_4853() =
        LocationExpectedRequest(
            latitude = -34.540412,
            longitude = -58.474732,
        )

    fun avCabildo_20() =
        LocationExpectedRequest(
            latitude = -34.574810,
            longitude = -58.435990,
        )

    fun virreyDelPino_1800() =
        LocationExpectedRequest(
            latitude = -34.562389,
            longitude = -58.445302,
        )
}
