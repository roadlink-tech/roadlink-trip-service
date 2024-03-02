package com.roadlink.tripservice.config

import com.roadlink.tripservice.domain.trip.events.CommandBus
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class CommandBusConfig {
    @Singleton
    fun commandBus(spyCommandBus: SpyCommandBus): CommandBus {
        return spyCommandBus
    }
}