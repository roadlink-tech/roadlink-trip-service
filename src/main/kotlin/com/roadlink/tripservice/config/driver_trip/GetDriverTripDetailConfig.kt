package com.roadlink.tripservice.config.driver_trip

import com.roadlink.tripservice.domain.common.utils.time.TimeProvider
import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.domain.trip_solicitude.TripLegSolicitudeRepository
import com.roadlink.tripservice.domain.user.UserRepository
import com.roadlink.tripservice.domain.user.UserTrustScoreRepository
import com.roadlink.tripservice.usecases.driver_trip.RetrieveDriverTripDetail
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class GetDriverTripDetailConfig {
    @Singleton
    fun getDriverTripDetail(
        sectionRepository: SectionRepository,
        tripLegSolicitudeRepository: TripLegSolicitudeRepository,
        userRepository: UserRepository,
        userTrustScoreRepository: UserTrustScoreRepository,
        timeProvider: TimeProvider,
    ): RetrieveDriverTripDetail {
        return RetrieveDriverTripDetail(
            sectionRepository = sectionRepository,
            tripLegSolicitudeRepository = tripLegSolicitudeRepository,
            userRepository = userRepository,
            userTrustScoreRepository = userTrustScoreRepository,
            timeProvider = timeProvider,
        )
    }
}