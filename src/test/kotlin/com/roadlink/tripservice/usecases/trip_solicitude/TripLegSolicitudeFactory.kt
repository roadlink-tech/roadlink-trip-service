package com.roadlink.tripservice.usecases.trip_solicitude

import com.roadlink.tripservice.domain.trip.section.Section
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude.*
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude.TripLegSolicitude.Status.PENDING_APPROVAL
import com.roadlink.tripservice.usecases.trip.SectionFactory
import java.util.*

object TripLegSolicitudeFactory {
    fun withSections(
        sections: List<Section>,
        status: TripLegSolicitude.Status = PENDING_APPROVAL,
        passengerId: String = "passengerId",
    ): TripLegSolicitude {
        return TripLegSolicitude(
            id = UUID.randomUUID(),
            sections = sections,
            status = status,
            passengerId = passengerId,
            authorizerId = "authorizerId"
        )
    }

    fun withDriver(
        driverId: UUID,
        tripId: UUID = UUID.randomUUID(),
        status: TripLegSolicitude.Status = PENDING_APPROVAL
    ): TripLegSolicitude {
        return TripLegSolicitude(
            id = UUID.randomUUID(),
            sections = listOf(
                SectionFactory.withDriver(
                    driverId = driverId,
                    tripId = tripId
                )
            ),
            status = status,
            passengerId = "passengerId",
            authorizerId = "authorizerId"
        )
    }

    fun any(): TripLegSolicitude {
        return TripLegSolicitude(
            id = UUID.randomUUID(),
            sections = listOf(
                SectionFactory.withDriver(
                    driverId = UUID.randomUUID(),
                )
            ),
            status = PENDING_APPROVAL,
            passengerId = "passengerId",
            authorizerId = "authorizerId"
        )
    }

}