package com.roadlink.tripservice.usecases.trip

import com.roadlink.tripservice.domain.trip.Trip
import com.roadlink.tripservice.usecases.common.trip_point.TripPointFactory

object TripFactory {

    const val avCabildo_id = "81dcb088-4b7e-4956-a50a-52eee0dd5a0b"
    const val avCabildo4853_virreyDelPino1800_avCabildo20_id = "TripFactory_avCabildo4853_virreyDelPino1800_avCabildo20"
    const val avCabildo4853_virreyDelPino1800_avCabildo20_uuid = "81dcb088-4b7e-4956-a50a-52eee0dd5a0c"


    fun avCabildo4853_to_avCabildo20(driverId: String = "John Smith", availableSeats: Int = 4) =
        Trip(
            id = avCabildo_id,
            driverId = driverId,
            vehicle = "Ford mustang",
            departure = TripPointFactory.avCabildo_4853(),
            arrival = TripPointFactory.avCabildo_20(),
            meetingPoints = emptyList(),
            availableSeats = availableSeats,
        )

    fun avCabildo4853_virreyDelPino1800_avCabildo20(
        id: String = "TripFactory_avCabildo4853_virreyDelPino1800_avCabildo20",
        driverId: String = "John Smith",
    ) =
        Trip(
            id = id,
            driverId = driverId,
            vehicle = "Ford mustang",
            departure = TripPointFactory.avCabildo_4853(),
            arrival = TripPointFactory.avCabildo_20(),
            meetingPoints = listOf(TripPointFactory.virreyDelPino_1800()),
            availableSeats = 5,
        )

    fun caba_escobar_pilar_rosario(
        id: String = "TripFactory_caba_escobar_pilar_rosario_id",
        driverId: String = "John Smith",
    ) =
        Trip(
            id = id,
            driverId = driverId,
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