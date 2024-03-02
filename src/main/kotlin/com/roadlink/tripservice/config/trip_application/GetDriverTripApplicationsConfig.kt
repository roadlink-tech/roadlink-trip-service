package com.roadlink.tripservice.config.trip_application

import com.roadlink.tripservice.domain.RatingRepository
import com.roadlink.tripservice.domain.user.UserRepository
import com.roadlink.tripservice.domain.trip_application.TripApplicationRepository
import com.roadlink.tripservice.usecases.trip_application.RetrieveDriverTripApplications
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class GetDriverTripApplicationsConfig {
    @Singleton
    fun getDriverTripApplications(
        tripApplicationRepository: TripApplicationRepository,
        userRepository: UserRepository,
        ratingRepository: RatingRepository,
    ): RetrieveDriverTripApplications {
        return RetrieveDriverTripApplications(
            tripApplicationRepository = tripApplicationRepository,
            userRepository = userRepository,
            ratingRepository = ratingRepository,
        )
    }
}