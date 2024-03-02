package com.roadlink.tripservice.config

import com.roadlink.tripservice.usecases.trip.SpyCommandBus
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class SpyCommandBusConfig {
    @Singleton
    fun spyCommandBus(): SpyCommandBus {
        return SpyCommandBus()
    }
}