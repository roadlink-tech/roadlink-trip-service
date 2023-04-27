package com.roadlink.tripservice.trip.domain

import com.roadlink.tripservice.usecases.JoinTripRequest

object JoinTripRequestFactory {

    const val avCabildo_id = "JoinTripRequestFactory-avCabildo"
    const val avCabildo4853_virreyDelPino1800_id = "SectionFactory-avCabildo4853_virreyDelPino1800"
    const val virreyDelPino1800_avCabildo20_id = "SectionFactory-virreyDelPino1800_avCabildo20"

    fun avCabildo(): JoinTripRequest =
        JoinTripRequest(
            id = avCabildo_id,
            passengerId = "passenger-id",
            sectionId = SectionFactory.avCabildo_id,
        )

    fun avCabildo4853_virreyDelPino1800(): JoinTripRequest =
        JoinTripRequest(
            id = avCabildo4853_virreyDelPino1800_id,
            passengerId = "passenger-id",
            sectionId = SectionFactory.avCabildo4853_virreyDelPino1800_id,
        )

    fun virreyDelPino1800_avCabildo20(): JoinTripRequest =
        JoinTripRequest(
            id = virreyDelPino1800_avCabildo20_id,
            passengerId = "passenger-id",
            sectionId = SectionFactory.virreyDelPino1800_avCabildo20_id,
        )

}