package com.roadlink.tripservice.usecases.trip_solicitude

import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitudeRepository
import com.roadlink.tripservice.usecases.UseCase
import java.util.UUID

// TODO TEST!!!
class RejectTripLegSolicitude(
    private val tripPlanSolicitudeRepository: TripPlanSolicitudeRepository
) : UseCase<UUID, RejectTripLegSolicitudeOutput> {

    override operator fun invoke(input: UUID): RejectTripLegSolicitudeOutput {
        val tripPlanSolicitude =
            tripPlanSolicitudeRepository.find(
                TripPlanSolicitudeRepository.CommandQuery(
                    tripLegSolicitudeId = input
                )
            )
                .firstOrNull()
                ?: return RejectTripLegSolicitudeOutput.TripPlanLegSolicitudeNotExists

        tripPlanSolicitude.reject()
        tripPlanSolicitudeRepository.update(tripPlanSolicitude)
        return RejectTripLegSolicitudeOutput.TripLegSolicitudeRejected
    }
}

sealed class RejectTripLegSolicitudeOutput {
    object TripPlanLegSolicitudeNotExists : RejectTripLegSolicitudeOutput()
    object TripLegSolicitudeRejected : RejectTripLegSolicitudeOutput()
}