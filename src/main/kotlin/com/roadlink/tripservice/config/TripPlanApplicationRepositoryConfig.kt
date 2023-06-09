package com.roadlink.tripservice.config

import com.roadlink.tripservice.domain.trip_application.TripPlanApplicationRepository
import com.roadlink.tripservice.infrastructure.persistence.trip_application.InMemoryTripPlanApplicationRepository
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class TripPlanApplicationRepositoryConfig {
    @Singleton
    fun tripPlanApplicationRepository(): TripPlanApplicationRepository {
        return InMemoryTripPlanApplicationRepository()
    }
}