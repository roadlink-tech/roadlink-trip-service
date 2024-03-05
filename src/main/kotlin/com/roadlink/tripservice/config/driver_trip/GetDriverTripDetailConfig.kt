package com.roadlink.tripservice.config.driver_trip

import com.roadlink.tripservice.domain.RatingRepository
import com.roadlink.tripservice.domain.common.utils.time.TimeProvider
import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.domain.trip_application.TripApplicationRepository
import com.roadlink.tripservice.domain.user.UserRepository
import com.roadlink.tripservice.usecases.driver_trip.RetrieveDriverTripDetail
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class GetDriverTripDetailConfig {
    @Singleton
    fun getDriverTripDetail(
        sectionRepository: SectionRepository,
        tripApplicationRepository: TripApplicationRepository,
        userRepository: UserRepository,
        ratingRepository: RatingRepository,
        timeProvider: TimeProvider,
    ): RetrieveDriverTripDetail {
        return RetrieveDriverTripDetail(
            sectionRepository = sectionRepository,
            tripApplicationRepository = tripApplicationRepository,
            userRepository = userRepository,
            ratingRepository = ratingRepository,
            timeProvider = timeProvider,
        )
    }
}