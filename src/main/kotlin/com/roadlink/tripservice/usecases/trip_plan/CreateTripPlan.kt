package com.roadlink.tripservice.usecases.trip_plan

import com.roadlink.tripservice.domain.trip_plan.TripPlan
import com.roadlink.tripservice.domain.trip_plan.TripPlanRepository
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude
import com.roadlink.tripservice.usecases.UseCase


class CreateTripPlan(private var repository: TripPlanRepository) :
    UseCase<CreateTripPlan.Input, CreateTripPlan.Output> {
    override fun invoke(input: Input): Output {
        val tripPlan = TripPlan.from(input.solicitude)
        repository.insert(tripPlan).also { return Output(tripPlan = it) }
    }

    class Input(
        var solicitude: TripPlanSolicitude
    )

    class Output(var tripPlan: TripPlan)
}