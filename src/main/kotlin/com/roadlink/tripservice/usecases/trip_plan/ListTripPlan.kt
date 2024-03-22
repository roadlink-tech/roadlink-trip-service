package com.roadlink.tripservice.usecases.trip_plan

import com.roadlink.tripservice.domain.trip_plan.TripPlan
import com.roadlink.tripservice.domain.trip_plan.TripPlanRepository
import com.roadlink.tripservice.usecases.UseCase
import java.util.*

class ListTripPlan(private var repository: TripPlanRepository) : UseCase<ListTripPlan.Input, ListTripPlan.Output> {

    override fun invoke(input: Input): Output {
        val tripPlans = repository.find(commandQuery = TripPlanRepository.CommandQuery(input.id))
        return Output(tripPlans)
    }

    class Input(
        var id: UUID? = null,
        var passengerId: UUID? = null
    )

    class Output(var tripPlans: List<TripPlan>)
}