package com.roadlink.tripservice.dev_tools.config

import com.roadlink.tripservice.dev_tools.infrastructure.HttpGeoapify
import com.roadlink.tripservice.dev_tools.usecases.AddSection
import com.roadlink.tripservice.domain.IdGenerator
import com.roadlink.tripservice.infrastructure.persistence.InMemorySectionRepository
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class AddSectionConfig {
    @Singleton
    fun addSection(
        inMemorySectionRepository: InMemorySectionRepository,
        httpGeoapify: HttpGeoapify,
        idGenerator: IdGenerator,
    ): AddSection {
        return AddSection(
            geoapify = httpGeoapify,
            sectionRepository = inMemorySectionRepository,
            idGenerator = idGenerator,
        )
    }
}