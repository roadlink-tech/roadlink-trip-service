package com.roadlink.tripservice.usecases.trip_application.plan

import com.roadlink.tripservice.domain.trip.section.Section
import com.roadlink.tripservice.domain.trip_application.TripPlanApplication
import com.roadlink.tripservice.domain.trip_application.TripPlanApplication.TripApplication
import com.roadlink.tripservice.domain.trip_application.TripPlanApplication.TripApplication.Status
import com.roadlink.tripservice.domain.trip_application.TripPlanApplication.TripApplication.Status.*
import com.roadlink.tripservice.usecases.factory.SectionFactory
import java.util.*

object TripPlanApplicationFactory {

    val johnSmithDriverId = UUID.fromString("806d4a73-eb1f-466f-a646-b71962df512c")

    fun completed(
        tripApplicationId: UUID = UUID.randomUUID(),
    ): TripPlanApplication {
        return TripPlanApplication(
            id = UUID.randomUUID(),
            tripApplications = mutableListOf(
                TripApplication(
                    id = tripApplicationId,
                    sections = listOf(
                        SectionFactory.avCabildo(
                            initialAmountOfSeats = 5,
                            bookedSeats = 5,
                            driverId = johnSmithDriverId.toString(),
                        ),
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
        driverId: String = johnSmithDriverId.toString()
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
        id: UUID = UUID.randomUUID(),
        tripApplicationId: UUID = UUID.randomUUID(),
        initialAmountOfSeats: Int = 4,
        driverId: String = johnSmithDriverId.toString(),
        passengerId: String = "passengerId",
    ): TripPlanApplication {
        return TripPlanApplication(
            id = id,
            tripApplications = mutableListOf(
                TripApplication(
                    id = tripApplicationId,
                    sections = listOf(
                        SectionFactory.avCabildo(
                            initialAmountOfSeats = initialAmountOfSeats,
                            driverId = driverId
                        )
                    ),
                    passengerId = passengerId,
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

    fun withApplications(
        tripApplications: List<TripApplication>
    ): TripPlanApplication {
        return TripPlanApplication(
            id = UUID.randomUUID(),
            tripApplications = tripApplications.toMutableList()
        )
    }

}