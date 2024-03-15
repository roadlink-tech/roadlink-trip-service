package com.roadlink.tripservice.infrastructure.factories

import com.roadlink.tripservice.infrastructure.rest.common.address.AddressResponse

object AddressResponseFactory {
    fun avCabildo_4853() =
        AddressResponse(
            location = LocationResponseFactory.avCabildo_4853(),
            fullAddress = "Av. Cabildo 4853, Buenos Aires",
            street = "Av. Cabildo",
            city = "Buenos Aires",
            country = "Argentina",
            housenumber = "4853",
        )

    fun avCabildo_20() =
        AddressResponse(
            location = LocationResponseFactory.avCabildo_20(),
            fullAddress = "Av. Cabildo 20, Buenos Aires",
            street = "Av. Cabildo",
            city = "Buenos Aires",
            country = "Argentina",
            housenumber = "20",
        )

    fun virreyDelPino1800() =
        AddressResponse(
            location = LocationResponseFactory.virreyDelPino1800(),
            fullAddress = "Virrey del Pino 1800, Buenos Aires",
            street = "Virrey del Pino",
            city = "Buenos Aires",
            country = "Argentina",
            housenumber = "1800",
        )

}