package com.roadlink.tripservice.config.trip_plan

import com.roadlink.tripservice.domain.trip_plan.TripPlanRepository
import com.roadlink.tripservice.usecases.trip_plan.RetrieveTripPlan
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class RetrieveTripPlanConfig {
    @Singleton
    fun retrieveTripPlan(tripPlanRepository: TripPlanRepository): RetrieveTripPlan {
        return RetrieveTripPlan(tripPlanRepository)
    }
}