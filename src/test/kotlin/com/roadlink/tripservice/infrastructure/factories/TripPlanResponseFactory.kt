package com.roadlink.tripservice.infrastructure.factories

import com.roadlink.tripservice.infrastructure.rest.trip_search.response.TripSearchPlanResponse
import com.roadlink.tripservice.usecases.trip.TripFactory

object TripPlanResponseFactory {
    fun avCabildo4853_virreyDelPino1800_avCabildo20(
        avCabildo4853_virreyDelPino1800_tripId: String = TripFactory.avCabildo_id,
        virreyDelPino1800_avCabildo20_tripId: String = TripFactory.avCabildo_id
    ) =
        TripSearchPlanResponse(
            sections = listOf(
                SectionResponseFactory.avCabildo4853_virreyDelPino1800(tripId = avCabildo4853_virreyDelPino1800_tripId),
                SectionResponseFactory.virreyDelPino1800_avCabildo20(tripId = virreyDelPino1800_avCabildo20_tripId),
            )
        )
}