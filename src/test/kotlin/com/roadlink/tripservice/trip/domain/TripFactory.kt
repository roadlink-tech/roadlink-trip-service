package com.roadlink.tripservice.trip.domain

import com.roadlink.tripservice.domain.trip.Trip

object TripFactory {

    const val avCabildo_id = "TripFactory_avCabildo"
    const val avCabildo4853_virreyDelPino1800_avCabildo20_id = "TripFactory_avCabildo4853_virreyDelPino1800_avCabildo20"
    const val caba_escobar_pilar_rosario_id = "TripFactory_caba_escobar_pilar_rosario_id"

    fun avCabildo() =
        Trip(
            id = avCabildo_id,
            driver = "John Smith",
            vehicle = "Ford mustang",
            departure = TripPointFactory.avCabildo_4853(),
            arrival = TripPointFactory.avCabildo_20(),
            meetingPoints = emptyList(),
            availableSeats = 4,
        )

    fun avCabildo4853_virreyDelPino1800_avCabildo20() =
        Trip(
            id = avCabildo4853_virreyDelPino1800_avCabildo20_id,
            driver = "John Smith",
            vehicle = "Ford mustang",
            departure = TripPointFactory.avCabildo_4853(),
            arrival = TripPointFactory.avCabildo_20(),
            meetingPoints = listOf(TripPointFactory.virreyDelPino_1800()),
            availableSeats = 5,
        )

    fun caba_escobar_pilar_rosario() =
        Trip(
            id = caba_escobar_pilar_rosario_id,
            driver = "John Smith",
            vehicle = "Ford mustang",
            departure = TripPointFactory.caba(),
            arrival = TripPointFactory.rosario(),
            meetingPoints = listOf(
                TripPointFactory.escobar(),
                TripPointFactory.pilar(),
            ),
            availableSeats = 5,
        )
}