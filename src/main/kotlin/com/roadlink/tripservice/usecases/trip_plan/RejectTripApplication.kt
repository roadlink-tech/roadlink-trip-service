package com.roadlink.tripservice.usecases.trip_plan

import com.roadlink.tripservice.domain.trip_application.TripPlanApplicationRepository
import com.roadlink.tripservice.usecases.UseCase
import java.util.UUID

// TODO TEST!!!
class RejectTripApplication(
    private val tripPlanApplicationRepository: TripPlanApplicationRepository
) : UseCase<UUID, RejectTripApplicationOutput> {

    override operator fun invoke(input: UUID): RejectTripApplicationOutput {
        val tripPlanApplication = tripPlanApplicationRepository.findByTripApplicationId(input)
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