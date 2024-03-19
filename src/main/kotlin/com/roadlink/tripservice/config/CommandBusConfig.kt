package com.roadlink.tripservice.config

import com.roadlink.tripservice.domain.common.IdGenerator
import com.roadlink.tripservice.domain.common.events.CommandBus
import com.roadlink.tripservice.domain.common.events.SimpleCommandBus
import com.roadlink.tripservice.domain.trip.events.OnTripCreatedEventCreateSections
import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.domain.trip_plan.events.OnTripLegSolicitudeAcceptedEventCreateTripPlan
import com.roadlink.tripservice.usecases.UseCase
import com.roadlink.tripservice.usecases.trip_plan.CreateTripPlan
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class CommandBusConfig {
    @Singleton
    fun commandBus(
        idGenerator: IdGenerator,
        sectionsRepository: SectionRepository,
        createTripPlan: UseCase<CreateTripPlan.Input, CreateTripPlan.Output>
    ): CommandBus {
        val bus = SimpleCommandBus()
        bus.registerHandler(
            OnTripCreatedEventCreateSections(
                idGenerator = idGenerator,
                sectionRepository = sectionsRepository,
            )
        )
        bus.registerHandler(
            OnTripLegSolicitudeAcceptedEventCreateTripPlan(
                createTripPlan
            )
        )
        return bus
    }
}