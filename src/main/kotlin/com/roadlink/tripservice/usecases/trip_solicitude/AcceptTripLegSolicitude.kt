package com.roadlink.tripservice.usecases.trip_solicitude

import com.roadlink.tripservice.domain.common.events.CommandBus
import com.roadlink.tripservice.domain.trip_plan.events.TripLegSolicitudeAcceptedEvent
import com.roadlink.tripservice.domain.trip_plan.events.TripLegSolicitudeEventResponse
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitudeRepository
import com.roadlink.tripservice.usecases.UseCase
import java.util.*

class AcceptTripLegSolicitude(
    private val tripPlanSolicitudeRepository: TripPlanSolicitudeRepository,
    private val commandBus: CommandBus,
) : UseCase<AcceptTripLegSolicitudeInput, AcceptTripLegSolicitudeOutput> {

    override operator fun invoke(input: AcceptTripLegSolicitudeInput): AcceptTripLegSolicitudeOutput {
        val tripPlanSolicitude = tripPlanSolicitudeRepository
            .find(TripPlanSolicitudeRepository.CommandQuery(tripLegSolicitudeId = input.tripLegSolicitudeId))
            .firstOrNull() ?: return AcceptTripLegSolicitudeOutput.TripPlanSolicitudeNotExists

        if (tripPlanSolicitude.isRejected()) {
            return AcceptTripLegSolicitudeOutput.TripLegSolicitudePlanHasBeenRejected
        }

        return AcceptTripLegSolicitudeOutput.TripLegSolicitudeAccepted.also {
            tripPlanSolicitude.acceptTripLegSolicitude(input.tripLegSolicitudeId, input.callerId)
            tripPlanSolicitudeRepository.update(tripPlanSolicitude)
        }.also {
            commandBus.publish<TripLegSolicitudeAcceptedEvent, TripLegSolicitudeEventResponse>(
                TripLegSolicitudeAcceptedEvent(solicitude = tripPlanSolicitude)
            )
        }
    }
}

data class AcceptTripLegSolicitudeInput(
    val tripLegSolicitudeId: UUID,
    // TODO fix it when the header has been change from the frontend
    val callerId: UUID? = null,
)

sealed class AcceptTripLegSolicitudeOutput {
    object TripPlanSolicitudeNotExists : AcceptTripLegSolicitudeOutput()
    object TripLegSolicitudePlanHasBeenRejected : AcceptTripLegSolicitudeOutput()
    object TripLegSolicitudeAccepted : AcceptTripLegSolicitudeOutput()
}
