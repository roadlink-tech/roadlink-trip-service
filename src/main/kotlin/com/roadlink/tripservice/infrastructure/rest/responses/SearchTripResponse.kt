package com.roadlink.tripservice.infrastructure.rest.responses

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.ALWAYS)
data class SearchTripResponse(val tripPlans: List<TripPlanResponse>)