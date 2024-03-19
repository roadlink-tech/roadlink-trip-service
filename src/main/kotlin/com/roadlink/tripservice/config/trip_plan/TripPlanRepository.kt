package com.roadlink.tripservice.config.trip_plan

import com.roadlink.tripservice.infrastructure.persistence.trip_plan.StubTripPlanRepository
import com.roadlink.tripservice.domain.trip_plan.TripPlanRepository
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class TripPlanRepository {

    @Singleton
    fun tripPlanRepository(): TripPlanRepository {
        return StubTripPlanRepository()
    }
}