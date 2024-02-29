package com.roadlink.tripservice.infrastructure.rest.trip_search.response

import com.fasterxml.jackson.annotation.JsonInclude
import com.roadlink.tripservice.infrastructure.rest.trip_plan.response.TripPlanResponse

@JsonInclude(JsonInclude.Include.ALWAYS)
data class SearchTripResponse(val tripPlans: List<TripPlanResponse>)