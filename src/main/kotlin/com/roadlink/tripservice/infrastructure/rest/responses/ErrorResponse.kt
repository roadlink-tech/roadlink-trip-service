package com.roadlink.tripservice.infrastructure.rest.responses

import com.roadlink.tripservice.infrastructure.rest.ApiResponse

abstract class ErrorResponse(val code: ErrorResponseCode) : ApiResponse
