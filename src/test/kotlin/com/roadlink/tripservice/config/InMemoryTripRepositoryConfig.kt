package com.roadlink.tripservice.config

import com.roadlink.tripservice.infrastructure.persistence.InMemoryTripRepository
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class InMemoryTripRepositoryConfig {
    @Singleton
    fun inMemoryTripRepository(): InMemoryTripRepository {
        return InMemoryTripRepository()
    }
}