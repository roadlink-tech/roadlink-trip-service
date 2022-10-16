package com.fdt.tripservice.domain

object TripPointFactory {
    fun avCabildo_4853() =
        TripPoint(
            location = LocationFactory.avCabildo_4853(),
            at = InstantFactory.october15_12hs(),
        )

    fun avCabildo_20() =
        TripPoint(
            location = LocationFactory.avCabildo_20(),
            at = InstantFactory.october15_18hs(),
        )

    fun virreyDelPino_2880() =
        TripPoint(
            location = LocationFactory.virreyDelPino_2880(),
            at = InstantFactory.october15_13hs(),
        )

    fun virreyDelPino_1800() =
        TripPoint(
            location = LocationFactory.virreyDelPino_1800(),
            at = InstantFactory.october15_17hs(),
        )

    fun avCabildo_1621() =
        TripPoint(
            location = LocationFactory.avCabildo_1621(),
            at = InstantFactory.october15_15hs(),
        )
}