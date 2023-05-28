package com.roadlink.tripservice.trip.domain

import com.roadlink.tripservice.domain.trip.section.Section

object SectionFactory {

    const val avCabildo_id = "SectionFactory-avCabildo"
    const val virreyDelPino_id = "SectionFactory-virreyDelPino"
    const val avCabildo4853_virreyDelPino1800_id = "SectionFactory-avCabildo4853_virreyDelPino1800"
    const val virreyDelPino1800_avCabildo20_id = "SectionFactory-virreyDelPino1800_avCabildo20"
    const val avCabildo4853_virreyDelPino2880_id = "SectionFactory-avCabildo4853_virreyDelPino2880"
    const val virreyDelPino2880_avCabildo20_id = "SectionFactory-virreyDelPino2880_avCabildo20"
    const val virreyDelPino2880_avCabildo1621_id = "SectionFactory-virreyDelPino2880_avCabildo1621"
    const val avCabildo1621_virreyDelPino1800_id = "SectionFactory-avCabildo1621_virreyDelPino1800"
    const val virreyDelPino1800_avDelLibertador5000_id = "SectionFactory-virreyDelPino1800_avDelLibertador5000"
    const val avDelLibertador5000_avCabildo20_id = "SectionFactory-avDelLibertador5000_avCabildo20"

    fun avCabildo(initialAmountOfSeats: Int = 4, bookedSeats: Int = 0) =
        Section(
            id = avCabildo_id,
            departure = TripPointFactory.avCabildo_4853(),
            arrival = TripPointFactory.avCabildo_20(),
            distanceInMeters = 6070.0,
            driver = "John Smith",
            vehicle = "Ford mustang",
            initialAmountOfSeats = initialAmountOfSeats,
            bookedSeats = bookedSeats
        )

    fun virreyDelPino() =
        Section(
            id = virreyDelPino_id,
            departure = TripPointFactory.virreyDelPino_2880(),
            arrival = TripPointFactory.virreyDelPino_1800(),
            distanceInMeters = 1300.0,
            driver = "John Smith",
            vehicle = "Ford mustang",
            initialAmountOfSeats = 4,
            bookedSeats = 0
        )

    fun avCabildo4853_virreyDelPino1800(availableSeats: Int = 4) =
        Section(
            id = avCabildo4853_virreyDelPino1800_id,
            departure = TripPointFactory.avCabildo_4853(),
            arrival = TripPointFactory.virreyDelPino_1800(),
            distanceInMeters = 4000.0,
            driver = "John Smith",
            vehicle = "Ford mustang",
            initialAmountOfSeats = availableSeats,
            bookedSeats = 0
        )

    fun virreyDelPino1800_avCabildo20() =
        Section(
            id = virreyDelPino1800_avCabildo20_id,
            departure = TripPointFactory.virreyDelPino_1800(),
            arrival = TripPointFactory.avCabildo_20(),
            distanceInMeters = 3000.0,
            driver = "John Smith",
            vehicle = "Ford mustang",
            initialAmountOfSeats = 4,
            bookedSeats = 0
        )

    fun avCabildo4853_virreyDelPino2880() =
        Section(
            id = avCabildo4853_virreyDelPino2880_id,
            departure = TripPointFactory.avCabildo_4853(),
            arrival = TripPointFactory.virreyDelPino_2880(),
            distanceInMeters = 5000.0,
            driver = "John Smith",
            vehicle = "Ford mustang",
            initialAmountOfSeats = 4,
            bookedSeats = 0
        )

    fun virreyDelPino2880_avCabildo20() =
        Section(
            id = virreyDelPino2880_avCabildo20_id,
            departure = TripPointFactory.virreyDelPino_2880(),
            arrival = TripPointFactory.avCabildo_20(),
            distanceInMeters = 3000.0,
            driver = "John Smith",
            vehicle = "Ford mustang",
            initialAmountOfSeats = 4,
            bookedSeats = 0
        )

    fun virreyDelPino2880_avCabildo1621() =
        Section(
            id = virreyDelPino2880_avCabildo1621_id,
            departure = TripPointFactory.virreyDelPino_2880(),
            arrival = TripPointFactory.avCabildo_1621(),
            distanceInMeters = 900.0,
            driver = "John Smith",
            vehicle = "Ford mustang",
            initialAmountOfSeats = 4,
            bookedSeats = 0
        )

    fun avCabildo1621_virreyDelPino1800() =
        Section(
            id = avCabildo1621_virreyDelPino1800_id,
            departure = TripPointFactory.avCabildo_1621(),
            arrival = TripPointFactory.virreyDelPino_1800(),
            distanceInMeters = 400.0,
            driver = "John Smith",
            vehicle = "Ford mustang",
            initialAmountOfSeats = 4,
            bookedSeats = 0
        )

    fun avCabildo1621_virreyDelPino1800_completed() =
        Section(
            id = avCabildo1621_virreyDelPino1800_id,
            departure = TripPointFactory.avCabildo_1621(),
            arrival = TripPointFactory.virreyDelPino_1800(),
            distanceInMeters = 400.0,
            driver = "John Smith",
            vehicle = "Ford mustang",
            initialAmountOfSeats = 0,
            bookedSeats = 0
        )

    fun virreyDelPino1800_avDelLibertador5000() =
        Section(
            id = virreyDelPino1800_avDelLibertador5000_id,
            departure = TripPointFactory.virreyDelPino_1800(),
            arrival = TripPointFactory.avDelLibertador_5000(),
            distanceInMeters = 3000.0,
            driver = "John Smith",
            vehicle = "Ford mustang",
            initialAmountOfSeats = 4,
            bookedSeats = 0
        )

    fun avDelLibertador5000_avCabildo20() =
        Section(
            id = avDelLibertador5000_avCabildo20_id,
            departure = TripPointFactory.avDelLibertador_5000(),
            arrival = TripPointFactory.avCabildo_20(),
            distanceInMeters = 3000.0,
            driver = "John Smith",
            vehicle = "Ford mustang",
            initialAmountOfSeats = 4,
            bookedSeats = 0
        )
}