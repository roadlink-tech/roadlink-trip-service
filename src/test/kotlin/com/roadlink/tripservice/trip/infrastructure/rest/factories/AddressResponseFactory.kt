package com.roadlink.tripservice.trip.infrastructure.rest.factories

import com.roadlink.tripservice.trip.infrastructure.rest.responses.AddressExpectedResponse

object AddressResponseFactory {
    fun avCabildo_4853() =
        AddressExpectedResponse(
            location = LocationResponseFactory.avCabildo_4853(),
            fullAddress = "Av. Cabildo 4853, Buenos Aires",
            street = "Av. Cabildo",
            city = "Buenos Aires",
            country = "Argentina",
            housenumber = "4853",
        )

    fun avCabildo_20() =
        AddressExpectedResponse(
            location = LocationResponseFactory.avCabildo_20(),
            fullAddress = "Av. Cabildo 20, Buenos Aires",
            street = "Av. Cabildo",
            city = "Buenos Aires",
            country = "Argentina",
            housenumber = "20",
        )

    fun virreyDelPino1800() =
        AddressExpectedResponse(
            location = LocationResponseFactory.virreyDelPino1800(),
            fullAddress = "Virrey del Pino 1800, Buenos Aires",
            street = "Virrey del Pino",
            city = "Buenos Aires",
            country = "Argentina",
            housenumber = "1800",
        )

}