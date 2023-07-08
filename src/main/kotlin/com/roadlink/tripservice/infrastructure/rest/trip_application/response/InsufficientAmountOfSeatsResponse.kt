package com.roadlink.tripservice.infrastructure.rest.trip_application.response

import com.roadlink.tripservice.infrastructure.rest.responses.ErrorResponse
import com.roadlink.tripservice.infrastructure.rest.responses.ErrorResponseCode.INSUFFICIENT_AMOUNT_OF_SEATS

class InsufficientAmountOfSeatsResponse : ErrorResponse(code = INSUFFICIENT_AMOUNT_OF_SEATS)
