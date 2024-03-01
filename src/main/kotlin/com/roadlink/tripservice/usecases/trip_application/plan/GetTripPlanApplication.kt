package com.roadlink.tripservice.usecases.trip_application.plan

import com.roadlink.tripservice.domain.trip_application.TripPlanApplication
import com.roadlink.tripservice.domain.trip_application.TripPlanApplicationRepository
import com.roadlink.tripservice.usecases.UseCase
import java.util.*

class GetTripPlanApplication(
    private val tripPlanApplicationRepository: TripPlanApplicationRepository
) : UseCase<GetTripPlanApplicationInput, GetTripPlanApplicationOutput> {

    override fun invoke(input: GetTripPlanApplicationInput): GetTripPlanApplicationOutput {
        val tripPlanApplication =
            tripPlanApplicationRepository.findByTripApplicationId(UUID.fromString(input.tripPlanApplicationId))
                ?: return GetTripPlanApplicationOutput.TripPlanApplicationNotFound
        return GetTripPlanApplicationOutput.TripPlanApplicationFound(tripPlanApplication)
    }
}

data class GetTripPlanApplicationInput(
    val tripPlanApplicationId: String,
)

sealed class GetTripPlanApplicationOutput {
    data class TripPlanApplicationFound(val tripPlanApplication: TripPlanApplication) : GetTripPlanApplicationOutput()
    object TripPlanApplicationNotFound : GetTripPlanApplicationOutput()
}
