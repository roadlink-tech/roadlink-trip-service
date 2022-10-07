package com.roadlink.tripservice.config

import com.roadlink.tripservice.infrastructure.repositories.InMemorySectionRepository
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class InMemorySectionRepositoryConfig {
    @Singleton
    fun inMemorySectionRepository(): InMemorySectionRepository {
        return InMemorySectionRepository()
    }
}