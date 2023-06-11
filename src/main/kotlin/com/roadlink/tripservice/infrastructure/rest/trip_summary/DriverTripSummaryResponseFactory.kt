package com.roadlink.tripservice.infrastructure.rest.trip_summary

import com.fasterxml.jackson.annotation.JsonProperty
import com.roadlink.tripservice.infrastructure.rest.ApiResponse
import com.roadlink.tripservice.usecases.trip_summary.RetrieveDriverTripSummaryOutput
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import java.time.Instant

/*
* JSONRESPONSE
* {
      [
        {
          "id": Int,
          "departure_district": String,
          "departure_instant": Instant,
          "arrival_district": String,
          "arrival_instant": Instant,
    	  "trip_status": String,
	      "has_seat_available": Boolean,
          "has_pending_notifications": Boolean
        }
      ]
   }
   * */
class DriverTripSummaryResponseFactory {
    fun from(output: RetrieveDriverTripSummaryOutput): HttpResponse<List<ApiResponse>> {
        output.trips.map { summary ->
            DriverTripSummaryResponse(
                id = summary.trip.id,
                departureDistrict = summary.trip.departure.address.city,
                arrivalDistrict = summary.trip.arrival.address.city,
                departureInstant = summary.trip.departure.estimatedArrivalTime,
                arrivalInstant = summary.trip.arrival.estimatedArrivalTime,
                // TODO which are the available values for this field, and why?
                status = "TODO it must be defined firstly",
                hasSeatAvailable = summary.hasAvailableSeats,
                // TODO what is this?
                hasPendingNotifications = false
            )
        }
        return HttpResponse.status<ApiResponse?>(HttpStatus.OK)
            .body(listOf<DriverTripSummaryResponse>())
    }
}

data class DriverTripSummaryResponse(
    val id: String,
    @JsonProperty("departure_district")
    val departureDistrict: String,
    @JsonProperty("departure_instant")
    val departureInstant: Instant,
    @JsonProperty("arrival_district")
    val arrivalDistrict: String,
    @JsonProperty("arrival_instant")
    val arrivalInstant: Instant,
    @JsonProperty("status")
    val status: String,
    @JsonProperty("has_seat_available")
    val hasSeatAvailable: Boolean,
    @JsonProperty("has_pending_notifications")
    val hasPendingNotifications: Boolean
) : ApiResponse
