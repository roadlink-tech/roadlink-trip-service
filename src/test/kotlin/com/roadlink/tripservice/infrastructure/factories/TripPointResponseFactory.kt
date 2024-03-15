package com.roadlink.tripservice.infrastructure.factories

import com.roadlink.tripservice.infrastructure.rest.common.trip_point.TripPointResponse
import com.roadlink.tripservice.usecases.factory.InstantFactory

object TripPointResponseFactory {
    fun avCabildo_4853() =
        TripPointResponse(
            estimatedArrivalTime = InstantFactory.october15_12hs().toEpochMilli(),
            address = AddressResponseFactory.avCabildo_4853(),
        )

    fun avCabildo_20() =
        TripPointResponse(
            estimatedArrivalTime = InstantFactory.october15_18hs().toEpochMilli(),
            address = AddressResponseFactory.avCabildo_20(),
        )

    fun virreyDelPino1800() =
        TripPointResponse(
            estimatedArrivalTime = InstantFactory.october15_17hs().toEpochMilli(),
            address = AddressResponseFactory.virreyDelPino1800(),
        )
}