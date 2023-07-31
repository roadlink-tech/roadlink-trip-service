package com.roadlink.tripservice.trip.domain

import com.roadlink.tripservice.domain.trip.section.Section
import com.roadlink.tripservice.domain.trip_application.TripPlanApplication
import com.roadlink.tripservice.domain.trip_application.TripPlanApplication.TripApplication
import com.roadlink.tripservice.domain.trip_application.TripPlanApplication.TripApplication.Status
import com.roadlink.tripservice.domain.trip_application.TripPlanApplication.TripApplication.Status.*
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
                    sections = listOf(
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
                    sections = listOf(
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
                    sections = listOf(
                        SectionFactory.avCabildo(initialAmountOfSeats = initialAmountOfSeats)
                    ),
                    passengerId = "passengerId",
                    authorizerId = "authorizerId"
                ),
            )
        )
    }

    fun withASingleTripApplicationRejected(
        sections: List<Section> = emptyList(),
        passengerId: String = "passengerId",
    ): TripPlanApplication {
        return TripPlanApplication(
            id = UUID.randomUUID(),
            tripApplications = mutableListOf(
                TripApplication(
                    id = UUID.randomUUID(),
                    sections = sections,
                    passengerId = passengerId,
                    status = REJECTED,
                    authorizerId = "authorizerId"
                ),
            )
        )
    }

    fun withASingleTripApplicationConfirmed(
        sections: List<Section> = emptyList(),
        passengerId: String = "passengerId",
    ): TripPlanApplication {
        return TripPlanApplication(
            id = UUID.randomUUID(),
            tripApplications = mutableListOf(
                TripApplication(
                    id = UUID.randomUUID(),
                    sections = sections,
                    passengerId = passengerId,
                    status = CONFIRMED,
                    authorizerId = "authorizerId"
                ),
            )
        )
    }

    fun withASingleTripApplicationPendingApproval(
        sections: List<Section> = emptyList(),
        passengerId: String = "passengerId",
    ): TripPlanApplication {
        return TripPlanApplication(
            id = UUID.randomUUID(),
            tripApplications = mutableListOf(
                TripApplication(
                    id = UUID.randomUUID(),
                    sections = sections,
                    passengerId = passengerId,
                    status = PENDING_APPROVAL,
                    authorizerId = "authorizerId"
                ),
            )
        )
    }
}