package com.roadlink.tripservice.usecases.common.trip_point

import com.roadlink.tripservice.domain.common.TripPoint
import com.roadlink.tripservice.usecases.common.address.AddressFactory
import com.roadlink.tripservice.usecases.common.InstantFactory
import java.time.Instant

object TripPointFactory {
    fun avCabildo_4853(at: Instant = InstantFactory.october15_12hs()): TripPoint {
        return TripPoint(
            estimatedArrivalTime = at,
            address = AddressFactory.avCabildo_4853(),
        )
    }

    fun avCabildo_20(at: Instant = InstantFactory.october15_18hs()) =
        TripPoint(
            estimatedArrivalTime = at,
            address = AddressFactory.avCabildo_20(),
        )

    fun virreyDelPino_2880() =
        TripPoint(
            estimatedArrivalTime = InstantFactory.october15_13hs(),
            address = AddressFactory.virreyDelPino_2880(),
        )

    fun virreyDelPino_1800(at: Instant = InstantFactory.october15_17hs()) =
        TripPoint(
            estimatedArrivalTime = at,
            address = AddressFactory.virreyDelPino_1800(),
        )

    fun caba(at: Instant = InstantFactory.october15_17hs()) =
        TripPoint(
            estimatedArrivalTime = at,
            address = AddressFactory.caba(),
        )

    fun escobar(at: Instant = InstantFactory.october15_17hs()) =
        TripPoint(
            estimatedArrivalTime = at,
            address = AddressFactory.escobar(),
        )

    fun pilar(at: Instant = InstantFactory.october15_17hs()) =
        TripPoint(
            estimatedArrivalTime = at,
            address = AddressFactory.pilar(),
        )

    fun rosario(at: Instant = InstantFactory.october15_17hs()) =
        TripPoint(
            estimatedArrivalTime = at,
            address = AddressFactory.rosario(),
        )

    fun avCabildo_1621() =
        TripPoint(
            estimatedArrivalTime = InstantFactory.october15_15hs(),
            address = AddressFactory.avCabildo_1621(),
        )

    fun avDelLibertador_5000() =
        TripPoint(
            estimatedArrivalTime = InstantFactory.october15_17_30hs(),
            address = AddressFactory.avDelLibertador_5000(),
        )
}