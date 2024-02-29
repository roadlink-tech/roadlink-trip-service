package com.roadlink.tripservice.infrastructure.rest.error

import com.roadlink.tripservice.infrastructure.rest.ApiResponse

abstract class ErrorResponse(val code: ErrorResponseCode) : ApiResponse
