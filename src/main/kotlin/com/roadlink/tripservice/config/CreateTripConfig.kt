package com.roadlink.tripservice.config

import com.roadlink.tripservice.domain.time.DefaultTimeProvider
import com.roadlink.tripservice.domain.trip.TripRepository
import com.roadlink.tripservice.domain.trip.events.CommandBus
import com.roadlink.tripservice.infrastructure.UUIDIdGenerator
import com.roadlink.tripservice.usecases.CreateTrip
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class CreateTripConfig {
    @Singleton
    fun createTrip(
        tripRepository: TripRepository,
        commandBus: CommandBus
    ): CreateTrip {
        return CreateTrip(
            tripRepository = tripRepository,
            idGenerator = UUIDIdGenerator(),
            commandBus = commandBus,
            timeProvider = DefaultTimeProvider()
        )
    }
}