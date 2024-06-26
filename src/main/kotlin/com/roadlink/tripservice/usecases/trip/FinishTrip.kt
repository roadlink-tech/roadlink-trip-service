package com.roadlink.tripservice.usecases.trip

import com.roadlink.tripservice.domain.trip.TripRepository
import com.roadlink.tripservice.domain.trip.feedback_solicitude.FeedbackSolicitude
import com.roadlink.tripservice.domain.trip.feedback_solicitude.FeedbackSolicitudeRepository
import com.roadlink.tripservice.domain.trip_plan.TripPlan
import com.roadlink.tripservice.domain.trip_plan.TripPlanRepository
import com.roadlink.tripservice.usecases.UseCase
import java.util.*

class FinishTrip(
    private val tripPlanRepository: TripPlanRepository,
    private val tripRepository: TripRepository,
    private val feedbackSolicitudeRepository: FeedbackSolicitudeRepository
) : UseCase<FinishTrip.Input, FinishTrip.Output> {

    override fun invoke(input: Input): Output {
        val tripPlans = tripPlanRepository.find(
            commandQuery = TripPlanRepository.CommandQuery(tripId = input.tripId)
        )

        val finishedTripLegs = tripPlans.map { tripPlan ->
            tripPlan.findLegByTripId(tripId = input.tripId).apply {
                finish()
                tripPlanRepository.update(tripPlan)
            }
        }

        tripRepository.find(TripRepository.CommandQuery(ids = listOf(input.tripId)))
            .firstOrNull()
            ?.finish()
            ?.update(tripRepository)

        val feedbackSolicitudes = createFeedbackSolicitude(tripPlans, input.tripId)
        feedbackSolicitudes.forEach(feedbackSolicitudeRepository::insert)

        return Output(feedbackSolicitudes, finishedTripLegs)
    }

    private fun createFeedbackSolicitude(
        tripPlans: List<TripPlan>,
        tripId: UUID
    ): List<FeedbackSolicitude> {
        val driverId = tripPlans.flatMap { it.tripLegs }
            .firstOrNull { it.tripId == tripId }
            ?.driverId

        val passengers: List<UUID> = tripPlans.map { it.passengerId }.let { passengerIds ->
            if (driverId != null) passengerIds + driverId else passengerIds
        }

        return passengers.flatMapIndexed { index, reviewerId ->
            passengers.drop(index + 1).flatMap { receiverId ->
                listOf(
                    FeedbackSolicitude(reviewerId, receiverId, tripId),
                    FeedbackSolicitude(receiverId, reviewerId, tripId)
                )
            }
        }
    }

    class Input(
        var tripId: UUID
    )

    class Output(
        var feedbackSolicitudes: List<FeedbackSolicitude>,
        var tripLegsFinished: List<TripPlan.TripLeg>
    )
}