package com.roadlink.tripservice.trip.domain

import com.roadlink.tripservice.domain.trip_application.TripPlanApplication
import java.util.*

object TripPlanApplicationFactory {

    fun withoutAvailableSeats(
        tripApplicationId: UUID = UUID.randomUUID(),
    ): TripPlanApplication {
        return TripPlanApplication(
            id = UUID.randomUUID(),
            tripApplications = mutableListOf(
                TripPlanApplication.TripApplication(
                    id = tripApplicationId,
                    sections = setOf(
                        SectionFactory.avCabildo(availableSeats = 0)
                    ),
                    passengerId = "passengerId",
                    authorizerId = "authorizerId"
                ),
            )
        )
    }

    fun withASingleTripApplication(
        tripApplicationId: UUID = UUID.randomUUID(),
        availableSeats: Int = 4
    ): TripPlanApplication {
        return TripPlanApplication(
            id = UUID.randomUUID(),
            tripApplications = mutableListOf(
                TripPlanApplication.TripApplication(
                    id = tripApplicationId,
                    sections = setOf(
                        SectionFactory.avCabildo(availableSeats = availableSeats)
                    ),
                    passengerId = "passengerId",
                    authorizerId = "authorizerId"
                ),
            )
        )
    }

    fun withAnApplicationRejected(): TripPlanApplication {
        return TripPlanApplication(
            id = UUID.randomUUID(),
            tripApplications = mutableListOf(
                TripPlanApplication.TripApplication(
                    id = UUID.randomUUID(),
                    sections = emptySet(),
                    passengerId = "passengerId",
                    status = TripPlanApplication.TripApplication.Status.REJECTED,
                    authorizerId = "authorizerId"
                ),
            )
        )
    }
}