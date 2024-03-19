package com.roadlink.tripservice.infrastructure.persistence.trip_plan

import com.roadlink.tripservice.domain.trip_plan.TripPlan
import com.roadlink.tripservice.domain.trip_plan.TripPlanRepository

class StubTripPlanRepository : TripPlanRepository {
    override fun insert(tripPlan: TripPlan): TripPlan {
        TODO("Not yet implemented")
    }
}