package com.roadlink.tripservice.trip.domain

import com.roadlink.tripservice.domain.trip.Address

object AddressFactory {
    fun avCabildo_4853() =
        Address(
            location = LocationFactory.avCabildo_4853(),
            fullAddress = "Av. Cabildo 4853, Buenos Aires",
            street = "Av. Cabildo",
            city = "Buenos Aires",
            country = "Argentina",
            houseNumber = "4853",
        )

    fun avCabildo_20() =
        Address(
            location = LocationFactory.avCabildo_20(),
            fullAddress = "Av. Cabildo 20, Buenos Aires",
            street = "Av. Cabildo",
            city = "Buenos Aires",
            country = "Argentina",
            houseNumber = "20",
        )

    fun virreyDelPino_2880() =
        Address(
            location = LocationFactory.virreyDelPino_2880(),
            fullAddress = "Virrey del Pino 2880, Buenos Aires",
            street = "Virrey del Pino",
            city = "Buenos Aires",
            country = "Argentina",
            houseNumber = "2880",
        )

    fun virreyDelPino_1800() =
        Address(
            location = LocationFactory.virreyDelPino_1800(),
            fullAddress = "Virrey del Pino 1800, Buenos Aires",
            street = "Virrey del Pino",
            city = "Buenos Aires",
            country = "Argentina",
            houseNumber = "1800",
        )

    fun caba() =
        Address(
            location = LocationFactory.caba(),
            fullAddress = "9 de julio y corrientes, Buenos Aires",
            street = "9 de julio",
            city = "CABA",
            country = "Argentina",
            houseNumber = "0",
        )

    fun escobar() =
        Address(
            location = LocationFactory.escobar(),
            fullAddress = "escobar centro",
            street = "escobar",
            city = "Escobar",
            country = "Argentina",
            houseNumber = "1800",
        )

    fun pilar() =
        Address(
            location = LocationFactory.pilar(),
            fullAddress = "Pilar centro, Buenos Aires",
            street = "Virrey del Pino",
            country = "Argentina",
            city = "Pilar",
            houseNumber = "1800",
        )

    fun rosario() =
        Address(
            location = LocationFactory.rosario(),
            fullAddress = "Rosario, Santa Fe",
            street = "Virrey del Pino",
            city = "Rosario",
            country = "Argentina",
            houseNumber = "1800",
        )

    fun avCabildo_1621() =
        Address(
            location = LocationFactory.avCabildo_1621(),
            fullAddress = "Av. Cabildo 1621, Buenos Aires",
            street = "Av. Cabildo",
            city = "Buenos Aires",
            country = "Argentina",
            houseNumber = "1621",
        )

    fun avDelLibertador_5000() =
        Address(
            location = LocationFactory.avDelLibertador_5000(),
            fullAddress = "Av. Del Libertador 5000, Buenos Aires",
            street = "Av. Del Libertador",
            city = "Buenos Aires",
            country = "Argentina",
            houseNumber = "5000",
        )
}