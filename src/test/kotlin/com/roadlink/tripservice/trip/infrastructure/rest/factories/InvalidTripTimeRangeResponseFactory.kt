package com.roadlink.tripservice.trip.infrastructure.rest.factories

import com.roadlink.tripservice.infrastructure.rest.error.handlers.InvalidTripTimeRangeResponse
import com.roadlink.tripservice.trip.domain.InstantFactory

object InvalidTripTimeRangeResponseFactory {
    fun avCabildo_invalidTimeRange() =
        InvalidTripTimeRangeResponse(
            actualTripPointEstimatedArrivalTime = InstantFactory.october15_12hs().toEpochMilli(),
            nextTripPointEstimatedArrivalTime = InstantFactory.october15_7hs().toEpochMilli(),
        )
}