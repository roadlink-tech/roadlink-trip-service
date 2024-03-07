package com.roadlink.tripservice.usecases.trip_solicitude

import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitudeRepository
import com.roadlink.tripservice.usecases.UseCase
import java.util.UUID

// TODO TEST!!!
class RejectTripLegSolicitude(
    private val tripPlanSolicitudeRepository: TripPlanSolicitudeRepository
) : UseCase<UUID, RejectTripLegSolicitudeOutput> {

    override operator fun invoke(input: UUID): RejectTripLegSolicitudeOutput {
        val tripPlanApplication =
            tripPlanSolicitudeRepository.find(TripPlanSolicitudeRepository.CommandQuery(tripApplicationId = input))
                .firstOrNull() ?: return RejectTripLegSolicitudeOutput.TripPlanLegSolicitudeNotExists

        tripPlanApplication.reject()
        tripPlanSolicitudeRepository.update(tripPlanApplication)
        return RejectTripLegSolicitudeOutput.TripLegSolicitudeRejected
    }
}

sealed class RejectTripLegSolicitudeOutput {
    object TripPlanLegSolicitudeNotExists : RejectTripLegSolicitudeOutput()
    object TripLegSolicitudeRejected : RejectTripLegSolicitudeOutput()
}