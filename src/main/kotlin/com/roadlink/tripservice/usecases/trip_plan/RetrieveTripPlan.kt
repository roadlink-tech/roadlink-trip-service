package com.roadlink.tripservice.usecases.trip_plan

import com.roadlink.tripservice.domain.trip_plan.TripPlan
import com.roadlink.tripservice.domain.trip_plan.TripPlanRepository
import com.roadlink.tripservice.usecases.UseCase
import java.util.UUID

class RetrieveTripPlan(private var repository: TripPlanRepository) : UseCase<RetrieveTripPlan.Input, RetrieveTripPlan.Output> {

    override fun invoke(input: Input): Output {
        val tripPlans = repository.find(TripPlanRepository.CommandQuery(id = input.tripPlanId))
        if (tripPlans.isEmpty()) {
            return Output.TripPlanNotFound
        }
        return Output.TripPlanFound(tripPlans.first())
    }

    data class Input(val tripPlanId: UUID)

    sealed class Output {
        data class TripPlanFound(val tripPlan: TripPlan) : Output()
        object TripPlanNotFound : Output()
    }

}