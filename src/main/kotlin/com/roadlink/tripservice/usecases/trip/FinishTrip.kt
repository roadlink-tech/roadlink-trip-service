package com.roadlink.tripservice.usecases.trip

import com.roadlink.tripservice.domain.trip.feedback_solicitude.FeedbackSolicitude
import com.roadlink.tripservice.domain.trip_plan.TripPlan
import com.roadlink.tripservice.domain.trip_plan.TripPlanRepository
import com.roadlink.tripservice.usecases.UseCase
import java.util.*

class FinishTrip(
    private val tripPlanRepository: TripPlanRepository,
) : UseCase<FinishTrip.Input, FinishTrip.Output> {

    override fun invoke(input: Input): Output {
        val tripPlans =
            tripPlanRepository.find(commandQuery = TripPlanRepository.CommandQuery(tripId = input.tripId))
        val feedbacksSolicitudes = createFeedbackSolicitude(tripPlans, input.tripId)
        tripPlans.forEach { tripPlan ->
            tripPlan.finishLegByTripId(tripId = input.tripId)
            // TODO it must be an updateAll
            tripPlanRepository.update(tripPlan)
        }
        return Output(feedbacksSolicitudes)
    }

    private fun createFeedbackSolicitude(
        tripPlans: List<TripPlan>,
        tripId: UUID
    ): List<FeedbackSolicitude> {
        val feedbackSolicitudes = mutableListOf<FeedbackSolicitude>()
        val passengers: List<UUID> = tripPlans.map { it.passengerId }
        val driverId = tripPlans.first().tripLegs.first { it.tripId == tripId }.driverId
        for (i in 0 until passengers.size - 1) {
            val reviewerId = passengers[i]
            for (j in i + 1 until passengers.size) {
                val receiverId = passengers[j]
                feedbackSolicitudes.add(
                    FeedbackSolicitude(
                        reviewerId = reviewerId,
                        receiverId = receiverId,
                        tripLegId = tripId
                    )
                )
            }
            feedbackSolicitudes.add(
                FeedbackSolicitude(
                    reviewerId = reviewerId,
                    receiverId = driverId,
                    tripLegId = tripId
                )
            )
        }
        return feedbackSolicitudes
    }

    class Input(
        var tripId: UUID
    )

    class Output(var feedbackSolicitudes: List<FeedbackSolicitude>)
}