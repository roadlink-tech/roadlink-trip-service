package com.roadlink.tripservice.infrastructure.rest.trip_search.response

import com.roadlink.tripservice.infrastructure.rest.common.trip_point.TripPointResponse


data class TripSearchPlanResponse(val sections: List<SectionResponse>)
data class SectionResponse(
    val id: String,
    val tripId: String,
    val departure: TripPointResponse,
    val arrival: TripPointResponse,
    val driver: String,
    val vehicle: String,
    val availableSeats: Int,
)