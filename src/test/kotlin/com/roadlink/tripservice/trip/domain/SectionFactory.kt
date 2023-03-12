package com.roadlink.tripservice.trip.domain

import com.roadlink.tripservice.domain.trip.section.Section

object SectionFactory {
    fun avCabildo() =
        Section(
            departure = TripPointFactory.avCabildo_4853(),
            arrival = TripPointFactory.avCabildo_20(),
            distanceInMeters = 6070.0,
            driver = "John Smith",
            vehicle = "Ford mustang",
            availableSeats = 4,
        )

    fun virreyDelPino() =
        Section(
            departure = TripPointFactory.virreyDelPino_2880(),
            arrival = TripPointFactory.virreyDelPino_1800(),
            distanceInMeters = 1300.0,
            driver = "John Smith",
            vehicle = "Ford mustang",
            availableSeats = 4,
        )

    fun avCabildo4853_virreyDelPino1800() =
        Section(
            departure = TripPointFactory.avCabildo_4853(),
            arrival = TripPointFactory.virreyDelPino_1800(),
            distanceInMeters = 4000.0,
            driver = "John Smith",
            vehicle = "Ford mustang",
            availableSeats = 4,
        )

    fun virreyDelPino1800_avCabildo20() =
        Section(
            departure = TripPointFactory.virreyDelPino_1800(),
            arrival = TripPointFactory.avCabildo_20(),
            distanceInMeters = 3000.0,
            driver = "John Smith",
            vehicle = "Ford mustang",
            availableSeats = 4,
        )

    fun avCabildo4853_virreyDelPino2880() =
        Section(
            departure = TripPointFactory.avCabildo_4853(),
            arrival = TripPointFactory.virreyDelPino_2880(),
            distanceInMeters = 5000.0,
            driver = "John Smith",
            vehicle = "Ford mustang",
            availableSeats = 4,
        )

    fun virreyDelPino2880_avCabildo20() =
        Section(
            departure = TripPointFactory.virreyDelPino_2880(),
            arrival = TripPointFactory.avCabildo_20(),
            distanceInMeters = 3000.0,
            driver = "John Smith",
            vehicle = "Ford mustang",
            availableSeats = 4,
        )

    fun virreyDelPino2880_avCabildo1621() =
        Section(
            departure = TripPointFactory.virreyDelPino_2880(),
            arrival = TripPointFactory.avCabildo_1621(),
            distanceInMeters = 900.0,
            driver = "John Smith",
            vehicle = "Ford mustang",
            availableSeats = 4,
        )

    fun avCabildo1621_virreyDelPino1800() =
        Section(
            departure = TripPointFactory.avCabildo_1621(),
            arrival = TripPointFactory.virreyDelPino_1800(),
            distanceInMeters = 400.0,
            driver = "John Smith",
            vehicle = "Ford mustang",
            availableSeats = 4,
        )

    fun virreyDelPino1800_avDelLibertador5000() =
        Section(
            departure = TripPointFactory.virreyDelPino_1800(),
            arrival = TripPointFactory.avDelLibertador_5000(),
            distanceInMeters = 3000.0,
            driver = "John Smith",
            vehicle = "Ford mustang",
            availableSeats = 4,
        )

    fun avDelLibertador5000_avCabildo20() =
        Section(
            departure = TripPointFactory.avDelLibertador_5000(),
            arrival = TripPointFactory.avCabildo_20(),
            distanceInMeters = 3000.0,
            driver = "John Smith",
            vehicle = "Ford mustang",
            availableSeats = 4,
        )
}