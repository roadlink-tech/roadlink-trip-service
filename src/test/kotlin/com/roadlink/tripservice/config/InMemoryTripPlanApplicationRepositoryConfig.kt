package com.roadlink.tripservice.config

import com.roadlink.tripservice.domain.trip_application.TripApplicationRepository
import com.roadlink.tripservice.infrastructure.persistence.trip_application.InMemoryTripPlanApplicationRepository
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class InMemoryTripPlanApplicationRepositoryConfig {
    @Singleton
    fun inMemoryTripPlanApplicationRepository(tripApplicationRepository: TripApplicationRepository): InMemoryTripPlanApplicationRepository {
        return InMemoryTripPlanApplicationRepository(tripApplicationRepository = tripApplicationRepository)
    }
}