package com.roadlink.tripservice.config

import com.roadlink.tripservice.domain.RatingRepository
import com.roadlink.tripservice.domain.UserRepository
import com.roadlink.tripservice.domain.trip_application.TripApplicationRepository
import com.roadlink.tripservice.usecases.trip_application.GetDriverTripApplications
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class GetDriverTripApplicationsConfig {
    @Singleton
    fun getDriverTripApplications(
        tripApplicationRepository: TripApplicationRepository,
        userRepository: UserRepository,
        ratingRepository: RatingRepository,
    ): GetDriverTripApplications {
        return GetDriverTripApplications(
            tripApplicationRepository = tripApplicationRepository,
            userRepository = userRepository,
            ratingRepository = ratingRepository,
        )
    }
}