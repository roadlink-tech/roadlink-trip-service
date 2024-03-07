package com.roadlink.tripservice.infrastructure.rest.trip_solicitude.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.roadlink.tripservice.usecases.trip_solicitude.plan.CreateTripPlanSolicitude

data class CreateTripPlanSolicitudeRequest(
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
        fun toInput(): CreateTripPlanSolicitude.Input.TripSections {
            return CreateTripPlanSolicitude.Input.TripSections(
                tripId = this.tripId,
                sectionsIds = this.sectionsIds.toSet()
            )
        }
    }
    fun toInput(): CreateTripPlanSolicitude.Input {
        return CreateTripPlanSolicitude.Input(
            passengerId = this.passengerId,
            trips = this.trips.map { it.toInput() }
        )
    }
}