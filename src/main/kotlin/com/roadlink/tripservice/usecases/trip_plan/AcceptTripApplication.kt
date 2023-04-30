package com.roadlink.tripservice.usecases.trip_plan

import com.roadlink.tripservice.domain.trip_application.TripPlanApplicationRepository
import java.util.*

class AcceptTripApplication(private val tripPlanApplicationRepository: TripPlanApplicationRepository) {

    operator fun invoke(tripApplicationId: UUID): AcceptTripApplicationOutput {
        val tripPlanApplication = tripPlanApplicationRepository.findByTripApplicationId(tripApplicationId)
            ?: return AcceptTripApplicationOutput.TripPlanApplicationNotExists

        if (tripPlanApplication.isRejected()) {
            return AcceptTripApplicationOutput.TripApplicationPlanHasBeenRejected
        }

        tripPlanApplication.confirmApplicationId(tripApplicationId)
        tripPlanApplicationRepository.save(tripPlanApplication)
        return AcceptTripApplicationOutput.TripApplicationAccepted
    }
}

sealed class AcceptTripApplicationOutput {
    object TripPlanApplicationNotExists : AcceptTripApplicationOutput()
    object TripApplicationPlanHasBeenRejected : AcceptTripApplicationOutput()
    object TripApplicationAccepted : AcceptTripApplicationOutput()
}
