package com.roadlink.tripservice.config

import com.roadlink.tripservice.domain.RatingRepository
import com.roadlink.tripservice.domain.UserRepository
import com.roadlink.tripservice.domain.time.TimeProvider
import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.domain.trip_application.TripPlanApplicationRepository
import com.roadlink.tripservice.usecases.GetDriverTripDetail
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class GetDriverTripDetailConfig {
    @Singleton
    fun getDriverTripDetail(
        sectionRepository: SectionRepository,
        tripPlanApplicationRepository: TripPlanApplicationRepository,
        userRepository: UserRepository,
        ratingRepository: RatingRepository,
        timeProvider: TimeProvider,
    ): GetDriverTripDetail {
        return GetDriverTripDetail(
            sectionRepository = sectionRepository,
            tripPlanApplicationRepository = tripPlanApplicationRepository,
            userRepository = userRepository,
            ratingRepository = ratingRepository,
            timeProvider = timeProvider,
        )
    }
}