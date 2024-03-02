package com.roadlink.tripservice.infrastructure.factories

import com.roadlink.tripservice.usecases.factory.InstantFactory
import com.roadlink.tripservice.infrastructure.requests.TripPointExpectedRequest
import java.time.Instant

object TripPointRequestFactory {
    fun avCabildo_4853() =
        TripPointExpectedRequest(
            estimatedArrivalTime = InstantFactory.october15_12hs().toString(),
            address = AddressRequestFactory.avCabildo_4853(),
        )

    fun avCabildo_20(estimatedArrivalTime: Instant = InstantFactory.october15_18hs()) =
        TripPointExpectedRequest(
            estimatedArrivalTime = estimatedArrivalTime.toString(),
            address = AddressRequestFactory.avCabildo_20(),
        )

    fun virreyDelPino_1800() =
        TripPointExpectedRequest(
            estimatedArrivalTime = InstantFactory.october15_17hs().toString(),
            address = AddressRequestFactory.virreyDelPino_1800(),
        )
}