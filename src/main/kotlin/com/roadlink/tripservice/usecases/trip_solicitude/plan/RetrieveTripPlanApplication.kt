package com.roadlink.tripservice.usecases.trip_solicitude.plan

import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitudeRepository
import com.roadlink.tripservice.usecases.UseCase
import java.util.*

class RetrieveTripPlanApplication(
    private val tripPlanSolicitudeRepository: TripPlanSolicitudeRepository
) : UseCase<RetrieveTripPlanSolicitudeInput, RetrieveTripPlanSolicitudeOutput> {

    override fun invoke(input: RetrieveTripPlanSolicitudeInput): RetrieveTripPlanSolicitudeOutput {
        val tripPlanApplications = tripPlanSolicitudeRepository.find(
            TripPlanSolicitudeRepository.CommandQuery(
                ids = listOf(
                    UUID.fromString(
                        input.tripPlanApplicationId
                    )
                )
            )
        )
        if (tripPlanApplications.isEmpty()) {
            return RetrieveTripPlanSolicitudeOutput.TripPlanSolicitudeNotFound
        }
        return RetrieveTripPlanSolicitudeOutput.TripPlanSolicitudeFound(tripPlanApplications.first())
    }
}

data class RetrieveTripPlanSolicitudeInput(
    val tripPlanApplicationId: String,
)

sealed class RetrieveTripPlanSolicitudeOutput {
    data class TripPlanSolicitudeFound(val tripPlanSolicitude: TripPlanSolicitude) :
        RetrieveTripPlanSolicitudeOutput()

    object TripPlanSolicitudeNotFound : RetrieveTripPlanSolicitudeOutput()
}
