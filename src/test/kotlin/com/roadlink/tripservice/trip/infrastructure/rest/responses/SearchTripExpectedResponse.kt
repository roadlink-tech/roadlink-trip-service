package com.roadlink.tripservice.trip.infrastructure.rest.responses

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.ALWAYS)
data class SearchTripExpectedResponse(val tripPlans: List<TripPlanExpectedResponse>)
