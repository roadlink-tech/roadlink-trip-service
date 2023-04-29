package com.roadlink.tripservice.infrastructure.rest.trip_application.response

import com.roadlink.tripservice.infrastructure.rest.ApiResponse
import com.roadlink.tripservice.infrastructure.rest.responses.ErrorResponse
import com.roadlink.tripservice.infrastructure.rest.responses.ErrorResponseCode
import com.roadlink.tripservice.infrastructure.rest.responses.ErrorResponseCode.TRIP_PLAN_COULD_NOT_BE_CREATED
import com.roadlink.tripservice.usecases.CreateTripPlanApplicationOutput
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import java.util.*
import  com.roadlink.tripservice.usecases.RejectTripApplicationOutput

class TripApplicationPlanResponseFactory {
    fun from(output: CreateTripPlanApplicationOutput): HttpResponse<ApiResponse> =
        when (output) {
            is CreateTripPlanApplicationOutput.TripPlanApplicationCreated ->
                HttpResponse.created(CreateTripPlanApplicationResponse.from(output))

            is CreateTripPlanApplicationOutput.OneOfTheSectionCanNotReceivePassenger -> HttpResponse.status<ApiResponse>(
                HttpStatus.PRECONDITION_FAILED
            ).body(CreateTripPlanApplicationErrorResponse(code = TRIP_PLAN_COULD_NOT_BE_CREATED))
        }

    fun from(output: RejectTripApplicationOutput): HttpResponse<ApiResponse> =
        when (output) {
            is RejectTripApplicationOutput.TripApplicationRejected ->
                HttpResponse.status(HttpStatus.ACCEPTED)
            is RejectTripApplicationOutput.TripPlanApplicationNotExists ->
                HttpResponse.status(HttpStatus.NOT_FOUND)
        }

}

class CreateTripPlanApplicationErrorResponse(code: ErrorResponseCode) : ErrorResponse(code)
data class CreateTripPlanApplicationResponse(
    val tripPlanApplicationId: UUID
) : ApiResponse {
    companion object {
        fun from(output: CreateTripPlanApplicationOutput.TripPlanApplicationCreated): CreateTripPlanApplicationResponse {
            return CreateTripPlanApplicationResponse(
                tripPlanApplicationId = output.tripPlanApplicationId
            )
        }
    }
}