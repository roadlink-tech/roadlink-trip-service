package com.roadlink.tripservice.dev_tools.config

import com.roadlink.tripservice.dev_tools.infrastructure.HttpGeoapify
import com.roadlink.tripservice.dev_tools.usecases.AddSection
import com.roadlink.tripservice.trip.infrastructure.persistence.InMemorySectionRepository
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class AddSectionConfig {
    @Singleton
    fun addSection(
        inMemorySectionRepository: InMemorySectionRepository,
        httpGeoapify: HttpGeoapify,
    ): AddSection {
        return AddSection(
            geoapify = httpGeoapify,
            sectionRepository = inMemorySectionRepository,
        )
    }
}