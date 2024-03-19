package com.roadlink.tripservice.domain.trip_plan.events

import com.roadlink.tripservice.domain.common.events.Command
import com.roadlink.tripservice.domain.common.events.CommandHandler
import com.roadlink.tripservice.domain.common.events.CommandResponse
import com.roadlink.tripservice.domain.trip_plan.TripPlan
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude
import com.roadlink.tripservice.usecases.UseCase
import com.roadlink.tripservice.usecases.trip_plan.CreateTripPlan

class OnTripLegSolicitudeAcceptedEventCreateTripPlan(
    private val createTripPlan: UseCase<CreateTripPlan.Input, CreateTripPlan.Output>,
) :
    CommandHandler<TripLegSolicitudeAcceptedEvent, TripLegSolicitudeEventResponse> {

    override fun handle(event: TripLegSolicitudeAcceptedEvent): TripLegSolicitudeEventResponse {
        if (event.solicitude.isAccepted()) {
            val output = createTripPlan(CreateTripPlan.Input(event.solicitude))
            return TripLegSolicitudeEventResponse.TripPlanCreated(output.tripPlan)
        }
        return TripLegSolicitudeEventResponse.TripPlanIsNotConfirmed
    }

}

/**
 * We sent the trip plan solicitude associated to the trip leg accepted
 */
data class TripLegSolicitudeAcceptedEvent(val solicitude: TripPlanSolicitude) : Command

sealed class TripLegSolicitudeEventResponse : CommandResponse {
    data class TripPlanCreated(val tripPlan: TripPlan) : TripLegSolicitudeEventResponse()
    object TripPlanIsNotConfirmed : TripLegSolicitudeEventResponse()
}