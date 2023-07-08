package com.roadlink.tripservice.trip.domain

import com.roadlink.tripservice.domain.trip_application.TripPlanApplication
import com.roadlink.tripservice.domain.trip_application.TripPlanApplication.TripApplication
import com.roadlink.tripservice.domain.trip_application.TripPlanApplication.TripApplication.Status
import com.roadlink.tripservice.domain.trip_application.TripPlanApplication.TripApplication.Status.PENDING_APPROVAL
import com.roadlink.tripservice.domain.trip_application.TripPlanApplication.TripApplication.Status.REJECTED
import java.util.*

object TripPlanApplicationFactory {

    fun completed(
        tripApplicationId: UUID = UUID.randomUUID(),
    ): TripPlanApplication {
        return TripPlanApplication(
            id = UUID.randomUUID(),
            tripApplications = mutableListOf(
                TripApplication(
                    id = tripApplicationId,
                    sections = setOf(
                        SectionFactory.avCabildo(initialAmountOfSeats = 5, bookedSeats = 5),
                    ),
                    passengerId = "passengerId",
                    authorizerId = "authorizerId"
                ),
            )
        )
    }

    fun withASingleBooking(
        tripApplicationId: UUID = UUID.randomUUID(),
        initialAmountOfSeats: Int = 4,
        tripId: UUID = UUID.randomUUID(),
        status: Status = PENDING_APPROVAL,
        driverId: String = "John Smith"
    ): TripPlanApplication {
        return TripPlanApplication(
            id = UUID.randomUUID(),
            tripApplications = mutableListOf(
                TripApplication(
                    id = tripApplicationId,
                    sections = setOf(
                        SectionFactory.avCabildo(
                            initialAmountOfSeats = initialAmountOfSeats,
                            bookedSeats = 1,
                            tripId = tripId,
                            driverId = driverId
                        )
                    ),
                    status = status,
                    passengerId = "passengerId",
                    authorizerId = "authorizerId"
                ),
            )
        )
    }

    fun withASingleTripApplication(
        tripApplicationId: UUID = UUID.randomUUID(),
        initialAmountOfSeats: Int = 4
    ): TripPlanApplication {
        return TripPlanApplication(
            id = UUID.randomUUID(),
            tripApplications = mutableListOf(
                TripApplication(
                    id = tripApplicationId,
                    sections = setOf(
                        SectionFactory.avCabildo(initialAmountOfSeats = initialAmountOfSeats)
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
                TripApplication(
                    id = UUID.randomUUID(),
                    sections = emptySet(),
                    passengerId = "passengerId",
                    status = REJECTED,
                    authorizerId = "authorizerId"
                ),
            )
        )
    }
}