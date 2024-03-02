package com.roadlink.tripservice.dev_tools.config

import com.roadlink.tripservice.dev_tools.infrastructure.HttpGeoapify
import com.roadlink.tripservice.dev_tools.usecases.AddSection
import com.roadlink.tripservice.domain.common.IdGenerator
import com.roadlink.tripservice.domain.trip.section.SectionRepository
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class AddSectionConfig {
    @Singleton
    fun addSection(
        sectionRepository: SectionRepository,
        httpGeoapify: HttpGeoapify,
        idGenerator: IdGenerator,
    ): AddSection {
        return AddSection(
            geoapify = httpGeoapify,
            sectionRepository = sectionRepository,
            idGenerator = idGenerator,
        )
    }
}