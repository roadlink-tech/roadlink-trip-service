package com.roadlink.tripservice.config.trip_plan

import com.roadlink.tripservice.domain.trip_plan.TripPlanRepository
import com.roadlink.tripservice.usecases.trip_plan.ListTripPlan
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class ListTripPlanConfig {

    @Singleton
    fun listTripPlan(tripPlanRepository: TripPlanRepository): ListTripPlan {
        return ListTripPlan(tripPlanRepository)
    }
}