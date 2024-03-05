package com.roadlink.tripservice.usecases.trip_application

import com.roadlink.tripservice.domain.trip_application.TripPlanApplicationRepository
import com.roadlink.tripservice.usecases.UseCase
import java.util.*

class AcceptTripApplication(
    private val tripPlanApplicationRepository: TripPlanApplicationRepository
) : UseCase<AcceptTripApplicationInput, AcceptTripApplicationOutput> {

    override operator fun invoke(input: AcceptTripApplicationInput): AcceptTripApplicationOutput {
        val tripPlanApplication = tripPlanApplicationRepository
            .find(TripPlanApplicationRepository.CommandQuery(tripApplicationId = input.tripApplicationId))
            .firstOrNull() ?: return AcceptTripApplicationOutput.TripPlanApplicationNotExists

        if (tripPlanApplication.isRejected()) {
            return AcceptTripApplicationOutput.TripApplicationPlanHasBeenRejected
        }

        return AcceptTripApplicationOutput.TripApplicationAccepted.also {
            tripPlanApplication.confirmApplicationById(input.tripApplicationId, input.callerId)
            tripPlanApplicationRepository.update(tripPlanApplication)
        }
    }
}

data class AcceptTripApplicationInput(
    val tripApplicationId: UUID,
    // TODO fix it when the header has been change from the frontend
    val callerId: UUID? = null,
)

sealed class AcceptTripApplicationOutput {
    object TripPlanApplicationNotExists : AcceptTripApplicationOutput()
    object TripApplicationPlanHasBeenRejected : AcceptTripApplicationOutput()
    object TripApplicationAccepted : AcceptTripApplicationOutput()
}
