package com.roadlink.tripservice.infrastructure.rest.trip_application.response

import com.roadlink.tripservice.infrastructure.rest.ApiResponse
import com.roadlink.tripservice.infrastructure.rest.error.ErrorResponse
import com.roadlink.tripservice.infrastructure.rest.error.ErrorResponseCode
import com.roadlink.tripservice.usecases.trip_application.AcceptTripApplicationOutput
import com.roadlink.tripservice.usecases.trip_application.RejectTripApplicationOutput
import com.roadlink.tripservice.usecases.trip_application.plan.CreateTripPlanApplication
import com.roadlink.tripservice.usecases.trip_application.plan.RetrieveTripPlanApplicationOutput
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus.*

class TripPlanApplicationResponseFactory {

    fun from(output: CreateTripPlanApplication.Output): HttpResponse<ApiResponse> =
        when (output) {
            is CreateTripPlanApplication.Output.TripPlanApplicationCreated ->
                HttpResponse.created(TripPlanApplicationCreatedResponse.from(output))

            is CreateTripPlanApplication.Output.OneOfTheSectionCanNotReceivePassenger ->
                HttpResponse
                    .status<InsufficientAmountOfSeatsResponse>(PRECONDITION_FAILED)
                    .body(InsufficientAmountOfSeatsResponse())
        }

    fun from(output: RetrieveTripPlanApplicationOutput): HttpResponse<ApiResponse> {
        return when (output) {
            is RetrieveTripPlanApplicationOutput.TripPlanApplicationFound ->
                HttpResponse.ok(TripPlanApplicationResponse.from(output.tripPlanApplication))

            is RetrieveTripPlanApplicationOutput.TripPlanApplicationNotFound ->
                HttpResponse.status(NOT_FOUND)
        }
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


class TripPlanApplicationHasBeenRejectedResponse :
    ErrorResponse(code = ErrorResponseCode.TRIP_PLAN_APPLICATION_HAS_BEEN_REJECTED)


class InsufficientAmountOfSeatsResponse : ErrorResponse(code = ErrorResponseCode.INSUFFICIENT_AMOUNT_OF_SEATS)