package com.roadlink.tripservice.trip.domain

import java.time.Instant

object TripPointFactory {
    fun avCabildo_4853(at: Instant = InstantFactory.october15_12hs()) =
        TripPoint(
            location = LocationFactory.avCabildo_4853(),
            at = at,
            formatted = "Av. Cabildo 4853, Buenos Aires",
            street = "Av. Cabildo",
            city = "Buenos Aires",
            country = "Argentina",
            housenumber = "4853",
        )

    fun avCabildo_20(at: Instant = InstantFactory.october15_18hs()) =
        TripPoint(
            location = LocationFactory.avCabildo_20(),
            at = at,
            formatted = "Av. Cabildo 20, Buenos Aires",
            street = "Av. Cabildo",
            city = "Buenos Aires",
            country = "Argentina",
            housenumber = "20",
        )

    fun virreyDelPino_2880() =
        TripPoint(
            location = LocationFactory.virreyDelPino_2880(),
            at = InstantFactory.october15_13hs(),
            formatted = "Virrey del Pino 2880, Buenos Aires",
            street = "Virrey del Pino",
            city = "Buenos Aires",
            country = "Argentina",
            housenumber = "2880",
        )

    fun virreyDelPino_1800(at: Instant = InstantFactory.october15_17hs()) =
        TripPoint(
            location = LocationFactory.virreyDelPino_1800(),
            at = at,
            formatted = "Virrey del Pino 1800, Buenos Aires",
            street = "Virrey del Pino",
            city = "Buenos Aires",
            country = "Argentina",
            housenumber = "1800",
        )

    fun avCabildo_1621() =
        TripPoint(
            location = LocationFactory.avCabildo_1621(),
            at = InstantFactory.october15_15hs(),
            formatted = "Av. Cabildo 1621, Buenos Aires",
            street = "Av. Cabildo",
            city = "Buenos Aires",
            country = "Argentina",
            housenumber = "1621",
        )

    fun avDelLibertador_5000() =
        TripPoint(
            location = LocationFactory.avDelLibertador_5000(),
            at = InstantFactory.october15_17_30hs(),
            formatted = "Av. Del Libertador 5000, Buenos Aires",
            street = "Av. Del Libertador",
            city = "Buenos Aires",
            country = "Argentina",
            housenumber = "5000",
        )
}