package com.roadlink.tripservice.usecases.trip_solicitude.plan

import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitudeRepository
import com.roadlink.tripservice.usecases.UseCase
import java.util.*

class ListTripPlanSolicitudes(
    private val tripPlanSolicitudeRepository: TripPlanSolicitudeRepository
) : UseCase<ListTripPlanSolicitudes.Input, ListTripPlanSolicitudes.Output> {

    override fun invoke(input: Input): Output {
        val tripPlanSolicitudes =
            tripPlanSolicitudeRepository.find(
                TripPlanSolicitudeRepository.CommandQuery(
                    passengerId = input.passengerId
                )
            )
        if (input.status() != null) {
            return Output(tripPlanSolicitudes.filter { it.status() == input.status() })
        }
        return Output(tripPlanSolicitudes)
    }

    data class Input(
        val passengerId: UUID,
        val status: String? = null
    ) {
        fun status(): TripPlanSolicitude.Status? {
            return status?.let {
                try {
                    TripPlanSolicitude.Status.valueOf(it.uppercase(Locale.getDefault()))
                } catch (e: IllegalArgumentException) {
                    null
                }
            }
        }
    }

    class Output(val tripPlanSolicitudes: List<TripPlanSolicitude>)

}