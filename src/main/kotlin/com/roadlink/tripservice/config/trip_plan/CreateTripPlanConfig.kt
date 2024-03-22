package com.roadlink.tripservice.config.trip_plan

import com.roadlink.tripservice.usecases.UseCase
import com.roadlink.tripservice.usecases.trip_plan.CreateTripPlan
import com.roadlink.tripservice.domain.trip_plan.TripPlanRepository
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class CreateTripPlanConfig {

    @Singleton
    fun createTripPlan(tripPlanRepository: TripPlanRepository): UseCase<CreateTripPlan.Input, CreateTripPlan.Output> {
        return CreateTripPlan(tripPlanRepository)
    }
}