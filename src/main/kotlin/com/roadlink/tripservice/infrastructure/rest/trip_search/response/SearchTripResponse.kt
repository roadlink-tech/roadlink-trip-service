package com.roadlink.tripservice.infrastructure.rest.trip_search.response

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.ALWAYS)
data class SearchTripResponse(val tripPlans: List<TripSearchPlanResponse>)