package com.roadlink.tripservice.usecases

import com.roadlink.tripservice.domain.trip_application.TripPlanApplicationRepository
import java.util.UUID

// TODO TEST!!!
class RejectTripApplication(
    private val tripPlanApplicationRepository: TripPlanApplicationRepository,
) {
    operator fun invoke(tripApplicationId: UUID): RejectTripApplicationOutput {
        val tripPlanApplication = tripPlanApplicationRepository.findByTripApplicationId(tripApplicationId)
            ?: return RejectTripApplicationOutput.TripPlanApplicationNotExists

        tripPlanApplication.reject()
        tripPlanApplicationRepository.save(tripPlanApplication)
        return RejectTripApplicationOutput.TripApplicationRejected
    }
}

sealed class RejectTripApplicationOutput {
    object TripPlanApplicationNotExists : RejectTripApplicationOutput()
    object TripApplicationRejected : RejectTripApplicationOutput()
}