package com.roadlink.tripservice.usecases.trip_solicitude.plan

import com.roadlink.tripservice.domain.trip.section.Section
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude.TripLegSolicitude
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude.TripLegSolicitude.Status
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude.TripLegSolicitude.Status.ACCEPTED
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude.TripLegSolicitude.Status.PENDING_APPROVAL
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude.TripLegSolicitude.Status.REJECTED
import com.roadlink.tripservice.usecases.trip.SectionFactory
import java.util.*

object TripPlanSolicitudeFactory {

    val johnSmithDriverId: UUID = UUID.fromString("806d4a73-eb1f-466f-a646-b71962df512c")

    fun common(
        id: UUID = UUID.randomUUID(),
        tripLegSolicitudes: List<TripLegSolicitude>
    ): TripPlanSolicitude {
        return TripPlanSolicitude(
            id = id,
            tripLegSolicitudes = tripLegSolicitudes.toMutableList()
        )
    }

    fun completed(
        tripApplicationId: UUID = UUID.randomUUID(),
    ): TripPlanSolicitude {
        return TripPlanSolicitude(
            id = UUID.randomUUID(),
            tripLegSolicitudes = mutableListOf(
                TripLegSolicitude(
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

    fun withASingleTripLegSolicitude(
        id: UUID = UUID.randomUUID(),
        tripLegSolicitudeId: UUID = UUID.randomUUID(),
        initialAmountOfSeats: Int = 4,
        driverId: String = johnSmithDriverId.toString(),
        passengerId: String = "passengerId",
        tripLegSolicitudeStatus: Status = PENDING_APPROVAL,
        vehicleId: String = "Ford mustang",
        tripId: UUID = UUID.randomUUID()
    ): TripPlanSolicitude {
        return TripPlanSolicitude(
            id = id,
            tripLegSolicitudes = mutableListOf(
                TripLegSolicitude(
                    id = tripLegSolicitudeId,
                    sections = listOf(
                        SectionFactory.avCabildo(
                            tripId = tripId,
                            initialAmountOfSeats = initialAmountOfSeats,
                            driverId = driverId,
                            vehicleId = vehicleId
                        )
                    ),
                    status = tripLegSolicitudeStatus,
                    passengerId = passengerId,
                    authorizerId = "authorizerId"
                ),
            )
        )
    }

    fun withASingleTripLegSolicitudeRejected(
        sections: List<Section> = emptyList(),
        passengerId: String = "passengerId",
    ): TripPlanSolicitude {
        return TripPlanSolicitude(
            id = UUID.randomUUID(),
            tripLegSolicitudes = mutableListOf(
                TripLegSolicitude(
                    id = UUID.randomUUID(),
                    sections = sections,
                    passengerId = passengerId,
                    status = REJECTED,
                    authorizerId = "authorizerId"
                ),
            )
        )
    }

    fun withASingleTripLegSolicitudeAccepted(
        sections: List<Section> = emptyList(),
        passengerId: String = "passengerId",
    ): TripPlanSolicitude {
        return TripPlanSolicitude(
            id = UUID.randomUUID(),
            tripLegSolicitudes = mutableListOf(
                TripLegSolicitude(
                    id = UUID.randomUUID(),
                    sections = sections,
                    passengerId = passengerId,
                    status = ACCEPTED,
                    authorizerId = "authorizerId"
                ),
            )
        )
    }

    fun withASingleTripLegSolicitude(
        tripLeg: TripLegSolicitude = TripLegSolicitude(
            id = UUID.randomUUID(),
            sections = listOf(SectionFactory.avCabildo1621_virreyDelPino1800()),
            passengerId = "passengerId",
            status = ACCEPTED,
            authorizerId = "authorizerId"
        ),
    ): TripPlanSolicitude {
        return TripPlanSolicitude(
            id = UUID.randomUUID(),
            tripLegSolicitudes = mutableListOf(tripLeg)
        )
    }

    fun withASingleTripLegSolicitudePendingApproval(
        sections: List<Section> = emptyList(),
        passengerId: String = "passengerId",
    ): TripPlanSolicitude {
        return TripPlanSolicitude(
            id = UUID.randomUUID(),
            tripLegSolicitudes = mutableListOf(
                TripLegSolicitude(
                    id = UUID.randomUUID(),
                    sections = sections,
                    passengerId = passengerId,
                    status = PENDING_APPROVAL,
                    authorizerId = "authorizerId"
                ),
            )
        )
    }

    fun withTripLegSolicitudes(
        tripLegSolicitudes: List<TripLegSolicitude>
    ): TripPlanSolicitude {
        return TripPlanSolicitude(
            id = UUID.randomUUID(),
            tripLegSolicitudes = tripLegSolicitudes.toMutableList()
        )
    }

}