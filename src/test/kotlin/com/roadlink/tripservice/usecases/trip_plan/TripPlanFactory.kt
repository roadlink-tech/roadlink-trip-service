package com.roadlink.tripservice.usecases.trip_plan

import com.roadlink.tripservice.domain.trip_plan.TripLegSection
import com.roadlink.tripservice.domain.trip_plan.TripPlan
import com.roadlink.tripservice.domain.trip_plan.TripPlan.Status
import com.roadlink.tripservice.domain.trip_plan.TripPlan.TripLeg
import com.roadlink.tripservice.usecases.common.trip_point.TripPointFactory
import java.util.*

object TripPlanFactory {

    fun common(
        id: UUID = UUID.randomUUID(),
        passengerId: UUID = UUID.randomUUID(),
        tripLegId: UUID = UUID.randomUUID(),
        tripId: UUID = UUID.randomUUID(),
        driverId: UUID = UUID.randomUUID(),
        vehicleId: UUID = UUID.randomUUID(),
        status: Status = Status.NOT_FINISHED,
        sections: List<TripLegSection> = emptyList(),
    ): TripPlan {
        return TripPlan(
            id = id,
            passengerId = passengerId,
            tripLegs = listOf(
                TripLeg(
                    id = tripLegId,
                    tripId = tripId,
                    vehicleId = vehicleId,
                    driverId = driverId,
                    sections = sections,
                    status = status
                )
            )
        )
    }
}