package com.roadlink.tripservice.config.trip

import com.roadlink.tripservice.domain.common.IdGenerator
import com.roadlink.tripservice.domain.common.utils.time.TimeProvider
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
        timeProvider: TimeProvider,
    ): CreateTrip {
        return CreateTrip(
            tripRepository = tripRepository,
            idGenerator = idGenerator,
            commandBus = commandBus,
            timeProvider = timeProvider,
        )
    }
}