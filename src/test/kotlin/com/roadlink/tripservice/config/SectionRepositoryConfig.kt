package com.roadlink.tripservice.config

import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.infrastructure.persistence.InMemorySectionRepository
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class SectionRepositoryConfig {
    @Singleton
    fun sectionRepository(inMemorySectionRepository: InMemorySectionRepository): SectionRepository {
        return inMemorySectionRepository
    }
}