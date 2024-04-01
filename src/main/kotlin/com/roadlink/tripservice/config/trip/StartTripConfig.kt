package com.roadlink.tripservice.config.trip

import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitudeRepository
import com.roadlink.tripservice.usecases.trip.StartTrip
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class StartTripConfig {
    @Singleton
    fun startTrip(
        tripPlanSolicitudeRepository: TripPlanSolicitudeRepository,
    ): StartTrip {
        return StartTrip(tripPlanSolicitudeRepository)
    }
}