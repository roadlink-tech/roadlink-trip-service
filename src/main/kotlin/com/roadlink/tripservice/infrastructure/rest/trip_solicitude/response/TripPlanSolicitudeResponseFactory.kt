package com.roadlink.tripservice.infrastructure.rest.trip_solicitude.response

import com.roadlink.tripservice.infrastructure.rest.ApiResponse
import com.roadlink.tripservice.infrastructure.rest.error.ErrorResponse
import com.roadlink.tripservice.infrastructure.rest.error.ErrorResponseCode
import com.roadlink.tripservice.usecases.trip_solicitude.AcceptTripLegSolicitudeOutput
import com.roadlink.tripservice.usecases.trip_solicitude.RejectTripLegSolicitudeOutput
import com.roadlink.tripservice.usecases.trip_solicitude.plan.CreateTripPlanSolicitude
import com.roadlink.tripservice.usecases.trip_solicitude.plan.RetrieveTripPlanSolicitudeOutput
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus.*

class TripPlanSolicitudeResponseFactory {

    fun from(output: CreateTripPlanSolicitude.Output): HttpResponse<ApiResponse> =
        when (output) {
            is CreateTripPlanSolicitude.Output.TripPlanSolicitudeCreated ->
                HttpResponse.created(TripPlanSolicitudeCreatedResponse.from(output))

            is CreateTripPlanSolicitude.Output.OneOfTheSectionCanNotReceivePassenger ->
                HttpResponse
                    .status<InsufficientAmountOfSeatsResponse>(PRECONDITION_FAILED)
                    .body(InsufficientAmountOfSeatsResponse())

            is CreateTripPlanSolicitude.Output.UserIsNotCompliantForJoiningTrip ->
                HttpResponse
                    .status<UserIsNotCompliantForJoiningTripResponse>(PRECONDITION_FAILED)
                    .body(UserIsNotCompliantForJoiningTripResponse())
        }

    fun from(output: RetrieveTripPlanSolicitudeOutput): HttpResponse<ApiResponse> {
        return when (output) {
            is RetrieveTripPlanSolicitudeOutput.TripPlanSolicitudeFound ->
                HttpResponse.ok(TripPlanSolicitudeResponse.from(output.tripPlanSolicitude))

            is RetrieveTripPlanSolicitudeOutput.TripPlanSolicitudeNotFound ->
                HttpResponse.status(NOT_FOUND)
        }
    }

    fun from(output: RejectTripLegSolicitudeOutput): HttpResponse<ApiResponse> =
        when (output) {
            is RejectTripLegSolicitudeOutput.TripLegSolicitudeRejected ->
                HttpResponse.status(ACCEPTED)

            is RejectTripLegSolicitudeOutput.TripPlanLegSolicitudeNotExists ->
                HttpResponse.status(NOT_FOUND)
        }

    fun from(output: AcceptTripLegSolicitudeOutput): HttpResponse<ApiResponse> =
        when (output) {
            is AcceptTripLegSolicitudeOutput.TripLegSolicitudeAccepted ->
                HttpResponse.status(ACCEPTED)

            is AcceptTripLegSolicitudeOutput.TripLegSolicitudePlanHasBeenRejected ->
                HttpResponse
                    .status<TripPlanSolicitudeHasBeenRejectedResponse>(PRECONDITION_FAILED)
                    .body(TripPlanSolicitudeHasBeenRejectedResponse())

            is AcceptTripLegSolicitudeOutput.TripPlanSolicitudeNotExists ->
                HttpResponse.status(NOT_FOUND)
        }

}


class TripPlanSolicitudeHasBeenRejectedResponse :
    ErrorResponse(code = ErrorResponseCode.TRIP_PLAN_SOLICITUDE_HAS_BEEN_REJECTED)

class InsufficientAmountOfSeatsResponse :
    ErrorResponse(code = ErrorResponseCode.INSUFFICIENT_AMOUNT_OF_SEATS)

class UserIsNotCompliantForJoiningTripResponse :
    ErrorResponse(code = ErrorResponseCode.USER_IS_NOT_COMPLIANT_FOR_JOINING_TRIP)