package com.roadlink.tripservice.usecases.trip_plan

import com.roadlink.tripservice.domain.trip_application.TripPlanApplicationRepository
import com.roadlink.tripservice.usecases.UseCase
import java.util.*

class AcceptTripApplication(
    private val tripPlanApplicationRepository: TripPlanApplicationRepository
) : UseCase<AcceptTripApplicationInput, AcceptTripApplicationOutput> {

    override operator fun invoke(input: AcceptTripApplicationInput): AcceptTripApplicationOutput {
        val tripPlanApplication = tripPlanApplicationRepository.findByTripApplicationId(input.tripApplication)
            ?: return AcceptTripApplicationOutput.TripPlanApplicationNotExists

        if (tripPlanApplication.isRejected()) {
            return AcceptTripApplicationOutput.TripApplicationPlanHasBeenRejected
        }

        tripPlanApplication.confirmApplicationById(input.tripApplication, input.callerId)
        return AcceptTripApplicationOutput.TripApplicationAccepted.also {
            tripPlanApplicationRepository.update(tripPlanApplication)
        }
    }
}

data class AcceptTripApplicationInput(
    val tripApplication: UUID,
    val callerId: UUID
)

sealed class AcceptTripApplicationOutput {
    object TripPlanApplicationNotExists : AcceptTripApplicationOutput()
    object TripApplicationPlanHasBeenRejected : AcceptTripApplicationOutput()
    object TripApplicationAccepted : AcceptTripApplicationOutput()
}
