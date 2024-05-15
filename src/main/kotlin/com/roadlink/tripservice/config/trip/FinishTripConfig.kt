package com.roadlink.tripservice.config.trip

import com.roadlink.tripservice.domain.trip.TripRepository
import com.roadlink.tripservice.domain.trip.feedback_solicitude.FeedbackSolicitudeRepository
import com.roadlink.tripservice.domain.trip_plan.TripPlanRepository
import com.roadlink.tripservice.usecases.trip.FinishTrip
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class FinishTripConfig {
    @Singleton
    fun finishTrip(
        tripPlanRepository: TripPlanRepository,
        tripRepository: TripRepository,
        feedbackSolicitudeRepository: FeedbackSolicitudeRepository
    ): FinishTrip {
        return FinishTrip(tripPlanRepository, tripRepository, feedbackSolicitudeRepository)
    }
}