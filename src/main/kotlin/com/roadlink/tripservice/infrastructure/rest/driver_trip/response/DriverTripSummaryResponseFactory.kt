package com.roadlink.tripservice.infrastructure.rest.driver_trip.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.roadlink.tripservice.infrastructure.rest.ApiResponse
import com.roadlink.tripservice.infrastructure.rest.error.ErrorResponse
import com.roadlink.tripservice.infrastructure.rest.error.ErrorResponseCode
import com.roadlink.tripservice.usecases.trip_summary.RetrieveDriverTripSummaryOutput
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus

class EmptyDriverTripSummaryResponse : ErrorResponse(code = ErrorResponseCode.EMPTY_DRIVER_TRIP_SUMMARY)

class DriverTripSummaryResponseFactory {
    fun from(output: RetrieveDriverTripSummaryOutput): HttpResponse<Any> {
        return when (output) {
            is RetrieveDriverTripSummaryOutput.DriverTripSummariesFound -> {
                val response: List<DriverTripSummaryResponse> = output.trips.map { summary ->
                    DriverTripSummaryResponse(
                        id = summary.trip.id,
                        departureDistrict = summary.trip.departure.address.city,
                        arrivalDistrict = summary.trip.arrival.address.city,
                        departureInstant = summary.trip.departure.estimatedArrivalTime.toEpochMilli(),
                        arrivalInstant = summary.trip.arrival.estimatedArrivalTime.toEpochMilli(),
                        status = summary.trip.status.toString(),
                        hasSeatAvailable = summary.hasAvailableSeats,
                        hasPendingApplications = summary.hasPendingApplications
                    )
                }
                HttpResponse.ok(response)
            }

            is RetrieveDriverTripSummaryOutput.DriverTripSummariesNotFound ->
                HttpResponse.status<EmptyDriverTripSummaryResponse>(HttpStatus.NOT_FOUND)
                    .body(EmptyDriverTripSummaryResponse())
        }
    }
}

data class DriverTripSummaryResponse(
    val id: String,
    @JsonProperty("departure_district")
    val departureDistrict: String,
    @JsonProperty("departure_instant")
    val departureInstant: Long,
    @JsonProperty("arrival_district")
    val arrivalDistrict: String,
    @JsonProperty("arrival_instant")
    val arrivalInstant: Long,
    @JsonProperty("status")
    val status: String,
    @JsonProperty("has_seat_available")
    val hasSeatAvailable: Boolean,
    @JsonProperty("has_pending_notifications") // ver con @Felix para que se has_pending_applications
    val hasPendingApplications: Boolean
) : ApiResponse
