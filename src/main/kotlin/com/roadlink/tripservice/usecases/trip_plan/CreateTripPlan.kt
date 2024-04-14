package com.roadlink.tripservice.usecases.trip_plan

import com.roadlink.tripservice.domain.trip_plan.TripPlan
import com.roadlink.tripservice.domain.trip_plan.TripPlanRepository
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude
import com.roadlink.tripservice.usecases.UseCase


class CreateTripPlan(private var repository: TripPlanRepository) :
    UseCase<CreateTripPlan.Input, CreateTripPlan.Output> {
    override fun invoke(input: Input): Output {
        val tripPlan = TripPlan.from(input.solicitude)
        repository.update(tripPlan)
            .also { return Output(tripPlan = tripPlan) } // update will change an existing entry when it exists or create a new one if it does not exist
    }

    class Input(
        var solicitude: TripPlanSolicitude
    )

    class Output(var tripPlan: TripPlan)
}