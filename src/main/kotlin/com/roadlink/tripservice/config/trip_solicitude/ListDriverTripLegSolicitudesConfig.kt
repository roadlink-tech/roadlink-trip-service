package com.roadlink.tripservice.config.trip_solicitude

import com.roadlink.tripservice.domain.RatingRepository
import com.roadlink.tripservice.domain.user.UserRepository
import com.roadlink.tripservice.domain.trip_solicitude.TripLegSolicitudeRepository
import com.roadlink.tripservice.usecases.driver_trip.ListDriverTripLegSolicitudes
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class ListDriverTripLegSolicitudesConfig {
    @Singleton
    fun listDriverTripLegSolicitudes(
        tripLegSolicitudeRepository: TripLegSolicitudeRepository,
        userRepository: UserRepository,
        ratingRepository: RatingRepository,
    ): ListDriverTripLegSolicitudes {
        return ListDriverTripLegSolicitudes(
            tripLegSolicitudeRepository = tripLegSolicitudeRepository,
            userRepository = userRepository,
            ratingRepository = ratingRepository,
        )
    }
}