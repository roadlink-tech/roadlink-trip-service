package com.roadlink.tripservice.usecases.trip_solicitude

import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitudeRepository
import com.roadlink.tripservice.usecases.UseCase
import java.util.*

class AcceptTripLegSolicitude(
    private val tripPlanSolicitudeRepository: TripPlanSolicitudeRepository
) : UseCase<AcceptTripLegSolicitudeInput, AcceptTripLegSolicitudeOutput> {

    override operator fun invoke(input: AcceptTripLegSolicitudeInput): AcceptTripLegSolicitudeOutput {
        val tripPlanApplication = tripPlanSolicitudeRepository
            .find(TripPlanSolicitudeRepository.CommandQuery(tripApplicationId = input.tripApplicationId))
            .firstOrNull() ?: return AcceptTripLegSolicitudeOutput.TripPlanSolicitudeNotExists

        if (tripPlanApplication.isRejected()) {
            return AcceptTripLegSolicitudeOutput.TripLegSolicitudePlanHasBeenRejected
        }

        return AcceptTripLegSolicitudeOutput.TripLegSolicitudeAccepted.also {
            tripPlanApplication.confirmApplicationById(input.tripApplicationId, input.callerId)
            tripPlanSolicitudeRepository.update(tripPlanApplication)
        }
    }
}

data class AcceptTripLegSolicitudeInput(
    val tripApplicationId: UUID,
    // TODO fix it when the header has been change from the frontend
    val callerId: UUID? = null,
)

sealed class AcceptTripLegSolicitudeOutput {
    object TripPlanSolicitudeNotExists : AcceptTripLegSolicitudeOutput()
    object TripLegSolicitudePlanHasBeenRejected : AcceptTripLegSolicitudeOutput()
    object TripLegSolicitudeAccepted : AcceptTripLegSolicitudeOutput()
}
