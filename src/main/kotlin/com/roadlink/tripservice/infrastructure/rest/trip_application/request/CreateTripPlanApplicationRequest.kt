package com.roadlink.tripservice.infrastructure.rest.trip_application.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.roadlink.tripservice.infrastructure.rest.trip_application.TripPlanApplicationDTO

data class CreateTripPlanApplicationRequest(
    @JsonProperty("passenger_id")
    val passengerId: String,
    @JsonProperty("trips")
    val trips: List<TripSectionsRequested>
) {
    data class TripSectionsRequested(
        @JsonProperty("trip_id")
        val tripId: String,
        @JsonProperty("section_ids")
        val sectionsIds: List<String>
    ) {
        fun toDTO(): TripPlanApplicationDTO.TripSectionsDTO {
            return TripPlanApplicationDTO.TripSectionsDTO(
                tripId = this.tripId,
                sectionsIds = this.sectionsIds
            )
        }
    }
    fun toDTO(): TripPlanApplicationDTO {
        return TripPlanApplicationDTO(
            passengerId = this.passengerId,
            trips = this.trips.map { it.toDTO() }
        )
    }
}