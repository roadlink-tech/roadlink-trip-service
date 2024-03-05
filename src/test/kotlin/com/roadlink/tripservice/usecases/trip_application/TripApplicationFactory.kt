package com.roadlink.tripservice.usecases.trip_application

import com.roadlink.tripservice.domain.trip.section.Section
import com.roadlink.tripservice.domain.trip_application.TripPlanApplication
import com.roadlink.tripservice.domain.trip_application.TripPlanApplication.TripApplication.Status.PENDING_APPROVAL
import com.roadlink.tripservice.usecases.factory.SectionFactory
import java.util.*

object TripApplicationFactory {
    fun withSections(sections: List<Section>): TripPlanApplication.TripApplication {
        return TripPlanApplication.TripApplication(
            id = UUID.randomUUID(),
            sections = sections,
            status = PENDING_APPROVAL,
            passengerId = "passengerId",
            authorizerId = "authorizerId"
        )
    }

    fun withDriver(driverId: UUID, tripId: UUID = UUID.randomUUID()): TripPlanApplication.TripApplication {
        return TripPlanApplication.TripApplication(
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

    fun any(): TripPlanApplication.TripApplication {
        return TripPlanApplication.TripApplication(
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