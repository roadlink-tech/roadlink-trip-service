package com.roadlink.tripservice.infrastructure.factories

import com.roadlink.tripservice.infrastructure.rest.error.handlers.InvalidTripTimeRangeResponse
import com.roadlink.tripservice.usecases.factory.InstantFactory

object InvalidTripTimeRangeResponseFactory {
    fun avCabildo_invalidTimeRange() =
        InvalidTripTimeRangeResponse(
            actualTripPointEstimatedArrivalTime = InstantFactory.october15_12hs().toEpochMilli(),
            nextTripPointEstimatedArrivalTime = InstantFactory.october15_7hs().toEpochMilli(),
        )
}