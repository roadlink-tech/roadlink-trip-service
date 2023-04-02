package com.roadlink.tripservice.trip.infrastructure.rest.factories

import com.roadlink.tripservice.trip.infrastructure.rest.requests.AddressExpectedRequest

object AddressRequestFactory {
    fun avCabildo_4853() =
        AddressExpectedRequest(
            location = LocationRequestFactory.avCabildo_4853(),
            street = "Av. Cabildo",
            city = "Buenos Aires",
            country = "Argentina",
            houseNumber = "4853",
        )

    fun avCabildo_20() =
        AddressExpectedRequest(
            location = LocationRequestFactory.avCabildo_20(),
            street = "Av. Cabildo",
            city = "Buenos Aires",
            country = "Argentina",
            houseNumber = "20",
        )

    fun virreyDelPino_1800() =
        AddressExpectedRequest(
            location = LocationRequestFactory.virreyDelPino_1800(),
            street = "Virrey del Pino",
            city = "Buenos Aires",
            country = "Argentina",
            houseNumber = "1800",
        )
}