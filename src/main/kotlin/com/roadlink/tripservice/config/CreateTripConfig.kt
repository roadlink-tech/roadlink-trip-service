package com.roadlink.tripservice.config

import com.roadlink.tripservice.domain.IdGenerator
import com.roadlink.tripservice.domain.time.DefaultTimeProvider
import com.roadlink.tripservice.domain.trip.TripRepository
import com.roadlink.tripservice.domain.trip.events.CommandBus
import com.roadlink.tripservice.usecases.trip.CreateTrip
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class CreateTripConfig {
    @Singleton
    fun createTrip(
        tripRepository: TripRepository,
        commandBus: CommandBus,
        idGenerator: IdGenerator,
    ): CreateTrip {
        return CreateTrip(
            tripRepository = tripRepository,
            idGenerator = idGenerator,
            commandBus = commandBus,
            timeProvider = DefaultTimeProvider()
        )
    }
}