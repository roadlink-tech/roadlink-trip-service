package com.roadlink.tripservice.trip.infrastructure.rest.factories

import com.roadlink.tripservice.trip.domain.InstantFactory
import com.roadlink.tripservice.trip.infrastructure.rest.requests.TripPointExpectedRequest

object TripPointRequestFactory {
    fun avCabildo_4853() =
        TripPointExpectedRequest(
            estimatedArrivalTime = InstantFactory.october15_12hs().toString(),
            address = AddressRequestFactory.avCabildo_4853(),
        )

    fun avCabildo_20() =
        TripPointExpectedRequest(
            estimatedArrivalTime = InstantFactory.october15_18hs().toString(),
            address = AddressRequestFactory.avCabildo_20(),
        )

    fun virreyDelPino_1800() =
        TripPointExpectedRequest(
            estimatedArrivalTime = InstantFactory.october15_17hs().toString(),
            address = AddressRequestFactory.virreyDelPino_1800(),
        )
}