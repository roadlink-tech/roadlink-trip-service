package com.roadlink.tripservice.usecases.trip_plan

import com.roadlink.tripservice.domain.trip_application.TripPlanApplicationRepository
import com.roadlink.tripservice.usecases.UseCase
import java.util.*

class AcceptTripApplication(
    private val tripPlanApplicationRepository: TripPlanApplicationRepository
) : UseCase<UUID, AcceptTripApplicationOutput> {

    override operator fun invoke(input: UUID): AcceptTripApplicationOutput {
        val tripPlanApplication = tripPlanApplicationRepository.findByTripApplicationId(input)
            ?: return AcceptTripApplicationOutput.TripPlanApplicationNotExists

        if (tripPlanApplication.isRejected()) {
            return AcceptTripApplicationOutput.TripApplicationPlanHasBeenRejected
        }

        tripPlanApplication.confirmApplicationId(input)
        tripPlanApplicationRepository.save(tripPlanApplication)
        return AcceptTripApplicationOutput.TripApplicationAccepted
    }
}

sealed class AcceptTripApplicationOutput {
    object TripPlanApplicationNotExists : AcceptTripApplicationOutput()
    object TripApplicationPlanHasBeenRejected : AcceptTripApplicationOutput()
    object TripApplicationAccepted : AcceptTripApplicationOutput()
}
