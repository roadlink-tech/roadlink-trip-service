package com.roadlink.tripservice.infrastructure.factories

import com.roadlink.tripservice.infrastructure.rest.trip.request.AddressRequest

object AddressRequestFactory {
    fun avCabildo_4853() =
        AddressRequest(
            location = LocationRequestFactory.avCabildo_4853(),
            street = "Av. Cabildo",
            city = "Buenos Aires",
            country = "Argentina",
            houseNumber = "4853",
        )

    fun avCabildo_20() =
        AddressRequest(
            location = LocationRequestFactory.avCabildo_20(),
            street = "Av. Cabildo",
            city = "Buenos Aires",
            country = "Argentina",
            houseNumber = "20",
        )

    fun virreyDelPino_1800() =
        AddressRequest(
            location = LocationRequestFactory.virreyDelPino_1800(),
            street = "Virrey del Pino",
            city = "Buenos Aires",
            country = "Argentina",
            houseNumber = "1800",
        )
}