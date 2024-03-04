package com.roadlink.tripservice.usecases.trip_application.plan

import com.roadlink.tripservice.domain.trip_application.TripPlanApplication
import com.roadlink.tripservice.domain.trip_application.TripPlanApplicationRepository
import com.roadlink.tripservice.usecases.UseCase
import java.util.*

class ListTripPlanApplication(
    private val tripPlanApplicationRepository: TripPlanApplicationRepository
) : UseCase<RetrieveTripPlanApplicationInput, RetrieveTripPlanApplicationOutput> {

    override fun invoke(input: RetrieveTripPlanApplicationInput): RetrieveTripPlanApplicationOutput {
        val tripPlanApplication =
            tripPlanApplicationRepository.findByTripApplicationId(UUID.fromString(input.tripPlanApplicationId))
                ?: return RetrieveTripPlanApplicationOutput.TripPlanApplicationNotFound
        return RetrieveTripPlanApplicationOutput.TripPlanApplicationFound(tripPlanApplication)
    }
}

//data class RetrieveTripPlanApplicationInput(
//    val tripPlanApplicationId: String,
//)
//
//sealed class RetrieveTripPlanApplicationOutput {
//    data class TripPlanApplicationFound(val tripPlanApplication: TripPlanApplication) : RetrieveTripPlanApplicationOutput()
//    object TripPlanApplicationNotFound : RetrieveTripPlanApplicationOutput()
//}
