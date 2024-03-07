package com.roadlink.tripservice.usecases.trip_solicitude

import com.roadlink.tripservice.domain.trip.section.Section
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude.TripLegSolicitude.Status.PENDING_APPROVAL
import com.roadlink.tripservice.usecases.factory.SectionFactory
import java.util.*

object TripApplicationFactory {
    fun withSections(sections: List<Section>): TripPlanSolicitude.TripLegSolicitude {
        return TripPlanSolicitude.TripLegSolicitude(
            id = UUID.randomUUID(),
            sections = sections,
            status = PENDING_APPROVAL,
            passengerId = "passengerId",
            authorizerId = "authorizerId"
        )
    }

    fun withDriver(driverId: UUID, tripId: UUID = UUID.randomUUID()): TripPlanSolicitude.TripLegSolicitude {
        return TripPlanSolicitude.TripLegSolicitude(
            id = UUID.randomUUID(),
            sections = listOf(
                SectionFactory.withDriver(
                    driverId = driverId,
                    tripId = tripId
                )
            ),
            status = PENDING_APPROVAL,
            passengerId = "passengerId",
            authorizerId = "authorizerId"
        )
    }

    fun any(): TripPlanSolicitude.TripLegSolicitude {
        return TripPlanSolicitude.TripLegSolicitude(
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