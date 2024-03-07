package com.roadlink.tripservice.config.trip_solicitude

import com.roadlink.tripservice.domain.RatingRepository
import com.roadlink.tripservice.domain.user.UserRepository
import com.roadlink.tripservice.domain.trip_solicitude.TripLegSolicitudeRepository
import com.roadlink.tripservice.usecases.driver_trip.ListDriverTripApplications
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class GetDriverTripApplicationsConfig {
    @Singleton
    fun getDriverTripApplications(
        tripLegSolicitudeRepository: TripLegSolicitudeRepository,
        userRepository: UserRepository,
        ratingRepository: RatingRepository,
    ): ListDriverTripApplications {
        return ListDriverTripApplications(
            tripLegSolicitudeRepository = tripLegSolicitudeRepository,
            userRepository = userRepository,
            ratingRepository = ratingRepository,
        )
    }
}