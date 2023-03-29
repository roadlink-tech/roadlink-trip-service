package com.roadlink.tripservice.config

import com.roadlink.tripservice.domain.trip.TripRepository
import com.roadlink.tripservice.infrastructure.persistence.InMemoryTripRepository
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class TripRepositoryConfig {
    @Singleton
    fun tripRepository(): TripRepository {
        return InMemoryTripRepository()
    }
}