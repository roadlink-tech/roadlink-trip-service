package com.roadlink.tripservice.usecases.trip_application.plan

import com.roadlink.tripservice.domain.trip_application.TripPlanApplication
import com.roadlink.tripservice.domain.trip_application.TripPlanApplicationRepository
import com.roadlink.tripservice.usecases.UseCase
import java.util.*

class ListTripPlanApplications(
    private val tripPlanApplicationRepository: TripPlanApplicationRepository
) : UseCase<ListTripPlanApplications.Input, ListTripPlanApplications.Output> {

    override fun invoke(input: Input): Output {
        val tripPlanApplications =
            tripPlanApplicationRepository.findAllByPassengerId(
                input.passengerId
            )
        if (input.status() != null) {
            return Output(tripPlanApplications.filter { it.status() == input.status() })
        }
        return Output(tripPlanApplications)
    }

    data class Input(
        val passengerId: UUID,
        val status: String? = null
    ) {
        fun status(): TripPlanApplication.Status? {
            return status?.let {
                try {
                    TripPlanApplication.Status.valueOf(it.uppercase(Locale.getDefault()))
                } catch (e: IllegalArgumentException) {
                    null
                }
            }
        }
    }

    class Output(val tripPlanApplications: List<TripPlanApplication>)

}