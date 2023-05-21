package com.roadlink.tripservice.infrastructure.rest.trip_application.response

import com.roadlink.tripservice.infrastructure.rest.ApiResponse
import com.roadlink.tripservice.usecases.trip_plan.AcceptTripApplicationOutput
import com.roadlink.tripservice.usecases.trip_plan.CreateTripPlanApplicationOutput
import com.roadlink.tripservice.usecases.trip_plan.RejectTripApplicationOutput
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus.*

class TripApplicationPlanResponseFactory {

    fun from(output: CreateTripPlanApplicationOutput): HttpResponse<ApiResponse> =
        when (output) {
            is CreateTripPlanApplicationOutput.TripPlanApplicationCreated ->
                HttpResponse.created(TripPlanApplicationCreatedResponse.from(output))

            is CreateTripPlanApplicationOutput.OneOfTheSectionCanNotReceivePassenger ->
                HttpResponse
                    .status<InsufficientAmountOfSeatsResponse>(PRECONDITION_FAILED)
                    .body(InsufficientAmountOfSeatsResponse())
        }

    fun from(output: RejectTripApplicationOutput): HttpResponse<ApiResponse> =
        when (output) {
            is RejectTripApplicationOutput.TripApplicationRejected ->
                HttpResponse.status(ACCEPTED)

            is RejectTripApplicationOutput.TripPlanApplicationNotExists ->
                HttpResponse.status(NOT_FOUND)
        }

    fun from(output: AcceptTripApplicationOutput): HttpResponse<ApiResponse> =
        when (output) {
            is AcceptTripApplicationOutput.TripApplicationAccepted ->
                HttpResponse.status(ACCEPTED)

            is AcceptTripApplicationOutput.TripApplicationPlanHasBeenRejected ->
                HttpResponse
                    .status<TripPlanApplicationHasBeenRejectedResponse>(PRECONDITION_FAILED)
                    .body(TripPlanApplicationHasBeenRejectedResponse())

            is AcceptTripApplicationOutput.TripPlanApplicationNotExists ->
                HttpResponse.status(NOT_FOUND)
        }

}
