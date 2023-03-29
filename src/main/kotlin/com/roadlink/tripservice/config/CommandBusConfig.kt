package com.roadlink.tripservice.config

import com.roadlink.tripservice.domain.trip.events.CommandBus
import com.roadlink.tripservice.domain.trip.events.SimpleCommandBus
import com.roadlink.tripservice.domain.trip.events.handlers.CreateTripHandler
import com.roadlink.tripservice.domain.trip.section.SectionRepository
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class CommandBusConfig {
    @Singleton
    fun commandBus(sectionsRepository: SectionRepository): CommandBus {
        val bus = SimpleCommandBus()
        bus.registerHandler(CreateTripHandler(sectionsRepository))
        return bus
    }
}