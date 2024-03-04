package com.roadlink.tripservice.usecases.trip_application.plan

import com.roadlink.tripservice.domain.trip_application.TripPlanApplication
import com.roadlink.tripservice.domain.trip_application.TripPlanApplication.*
import com.roadlink.tripservice.domain.trip_application.TripPlanApplicationRepository
import com.roadlink.tripservice.infrastructure.rest.trip_application.response.TripApplicationStatusResponse
import com.roadlink.tripservice.usecases.UseCase
import java.util.*

class ListTripPlanApplications(
    private val tripPlanApplicationRepository: TripPlanApplicationRepository
) : UseCase<ListTripPlanApplications.Input, ListTripPlanApplications.Output> {

    override fun invoke(input: Input): Output {
        val tripPlanApplications =
            tripPlanApplicationRepository.findAllByPassengerIdAndTripApplicationStatus(
                input.passengerId,
                input.toTripApplicationStatus()
            )
        return Output(tripPlanApplications)
    }

    data class Input(
        val passengerId: UUID,
        val tripApplicationStatus: String? = null
    ) {
        fun toTripApplicationStatus(): TripApplication.Status? {
            return tripApplicationStatus?.let {
                try {
                    TripPlanApplication.TripApplication.Status.valueOf(it.uppercase(Locale.getDefault()))
                } catch (e: IllegalArgumentException) {
                    null
                }
            }
        }
    }

    class Output(val tripPlanApplications: List<TripPlanApplication>)

}