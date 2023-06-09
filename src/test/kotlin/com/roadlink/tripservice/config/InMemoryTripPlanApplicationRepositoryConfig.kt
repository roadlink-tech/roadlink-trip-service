package com.roadlink.tripservice.config

import com.roadlink.tripservice.infrastructure.persistence.trip_application.InMemoryTripPlanApplicationRepository
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class InMemoryTripPlanApplicationRepositoryConfig {
    @Singleton
    fun inMemoryTripPlanApplicationRepository(): InMemoryTripPlanApplicationRepository {
        return InMemoryTripPlanApplicationRepository()
    }
}