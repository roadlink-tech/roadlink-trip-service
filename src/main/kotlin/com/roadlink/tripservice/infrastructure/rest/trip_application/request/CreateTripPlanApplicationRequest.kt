package com.roadlink.tripservice.infrastructure.rest.trip_application.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.roadlink.tripservice.usecases.trip_application.plan.CreateTripPlanApplication

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
        fun toInput(): CreateTripPlanApplication.Input.TripSections {
            return CreateTripPlanApplication.Input.TripSections(
                tripId = this.tripId,
                sectionsIds = this.sectionsIds.toSet()
            )
        }
    }
    fun toInput(): CreateTripPlanApplication.Input {
        return CreateTripPlanApplication.Input(
            passengerId = this.passengerId,
            trips = this.trips.map { it.toInput() }
        )
    }
}