package com.roadlink.tripservice.config

import com.roadlink.tripservice.domain.trip_application.TripApplicationRepository
import com.roadlink.tripservice.infrastructure.persistence.trip_application.InMemoryTripApplicationRepository
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class TripApplicationRepositoryConfig {
    @Singleton
    fun tripApplicationRepository(inMemoryTipApplicationRepository: InMemoryTripApplicationRepository): TripApplicationRepository {
        return inMemoryTipApplicationRepository
    }
}