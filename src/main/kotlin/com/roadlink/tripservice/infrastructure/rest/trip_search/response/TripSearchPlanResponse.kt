package com.roadlink.tripservice.infrastructure.rest.trip_search.response

import com.roadlink.tripservice.domain.trip.section.Section
import com.roadlink.tripservice.infrastructure.rest.common.trip_point.TripPointResponse


data class TripSearchPlanResponse(val sections: List<SectionResponse>)
data class SectionResponse(
    val id: String,
    val tripId: String,
    val departure: TripPointResponse,
    val arrival: TripPointResponse,
    // TODO es un id!
    val driver: String,
    // TODO es un id!
    val vehicle: String,
    val availableSeats: Int,
) {
    companion object {
        fun from(section: Section): SectionResponse {
            return SectionResponse(
                id = section.id,
                tripId = section.tripId.toString(),
                departure = TripPointResponse.from(section.departure),
                arrival = TripPointResponse.from(section.arrival),
                driver = section.driverId,
                vehicle = section.vehicleId,
                availableSeats = section.availableSeats(),
            )
        }
    }
}