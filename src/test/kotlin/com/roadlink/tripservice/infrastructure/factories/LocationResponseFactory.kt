package com.roadlink.tripservice.infrastructure.factories

import com.roadlink.tripservice.infrastructure.rest.common.address.LocationResponse

object LocationResponseFactory {
    fun avCabildo_4853() =
        LocationResponse(
            latitude = -34.540412,
            longitude = -58.474732,
        )

    fun avCabildo_20() =
        LocationResponse(
            latitude = -34.574810,
            longitude = -58.435990,
        )

    fun virreyDelPino1800() =
        LocationResponse(
            latitude = -34.562389,
            longitude = -58.445302,
        )
}