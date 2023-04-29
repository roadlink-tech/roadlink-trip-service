package com.roadlink.tripservice.infrastructure.rest.trip_application

data class TripPlanApplicationDTO(
    val passengerId: String,
    val trips: List<TripSectionsDTO>
) {
    data class TripSectionsDTO(
        val tripId: String,
        val sectionsIds: List<String>
    )
}