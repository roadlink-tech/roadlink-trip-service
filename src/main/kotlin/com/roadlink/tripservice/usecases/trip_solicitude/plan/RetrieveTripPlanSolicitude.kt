package com.roadlink.tripservice.usecases.trip_solicitude.plan

import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitudeRepository
import com.roadlink.tripservice.usecases.UseCase
import java.util.*

class RetrieveTripPlanSolicitude(
    private val tripPlanSolicitudeRepository: TripPlanSolicitudeRepository
) : UseCase<RetrieveTripPlanSolicitudeInput, RetrieveTripPlanSolicitudeOutput> {

    override fun invoke(input: RetrieveTripPlanSolicitudeInput): RetrieveTripPlanSolicitudeOutput {
        val tripPlanSolicitudes = tripPlanSolicitudeRepository.find(
            TripPlanSolicitudeRepository.CommandQuery(
                ids = listOf(
                    UUID.fromString(
                        input.tripPlanSolicitudeId
                    )
                )
            )
        )
        if (tripPlanSolicitudes.isEmpty()) {
            return RetrieveTripPlanSolicitudeOutput.TripPlanSolicitudeNotFound
        }
        return RetrieveTripPlanSolicitudeOutput.TripPlanSolicitudeFound(tripPlanSolicitudes.first())
    }
}

data class RetrieveTripPlanSolicitudeInput(
    val tripPlanSolicitudeId: String,
)

sealed class RetrieveTripPlanSolicitudeOutput {
    data class TripPlanSolicitudeFound(val tripPlanSolicitude: TripPlanSolicitude) :
        RetrieveTripPlanSolicitudeOutput()

    object TripPlanSolicitudeNotFound : RetrieveTripPlanSolicitudeOutput()
}
