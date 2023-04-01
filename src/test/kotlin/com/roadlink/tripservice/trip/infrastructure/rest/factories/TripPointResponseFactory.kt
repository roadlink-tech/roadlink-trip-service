package com.roadlink.tripservice.trip.infrastructure.rest.factories

import com.roadlink.tripservice.trip.domain.InstantFactory
import com.roadlink.tripservice.trip.infrastructure.rest.responses.TripPointExpectedResponse

object TripPointResponseFactory {
        fun avCabildo_4853() =
        TripPointExpectedResponse(
            location = LocationResponseFactory.avCabildo_4853(),
            at = InstantFactory.october15_12hs().toEpochMilli(),
            formatted = "Av. Cabildo 4853, Buenos Aires",
            street = "Av. Cabildo",
            city = "Buenos Aires",
            country = "Argentina",
            housenumber = "4853",
        )

    fun avCabildo_20() =
        TripPointExpectedResponse(
            location = LocationResponseFactory.avCabildo_20(),
            at = InstantFactory.october15_18hs().toEpochMilli(),
            formatted = "Av. Cabildo 20, Buenos Aires",
            street = "Av. Cabildo",
            city = "Buenos Aires",
            country = "Argentina",
            housenumber = "20",
        )

    fun virreyDelPino1800() =
        TripPointExpectedResponse(
            location = LocationResponseFactory.virreyDelPino1800(),
            at = InstantFactory.october15_17hs().toEpochMilli(),
            formatted = "Virrey del Pino 1800, Buenos Aires",
            street = "Virrey del Pino",
            city = "Buenos Aires",
            country = "Argentina",
            housenumber = "1800",
        )
}