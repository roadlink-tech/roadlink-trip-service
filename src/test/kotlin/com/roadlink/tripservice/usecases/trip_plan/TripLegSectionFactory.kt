package com.roadlink.tripservice.usecases.trip_plan

import com.roadlink.tripservice.domain.common.TripPoint
import com.roadlink.tripservice.domain.trip_plan.TripLegSection
import com.roadlink.tripservice.usecases.common.trip_point.TripPointFactory
import java.util.UUID

object TripLegSectionFactory {

    fun common(
        id: UUID = UUID.randomUUID(),
        departure: TripPoint = TripPointFactory.avCabildo_1621(),
        arrival: TripPoint = TripPointFactory.avCabildo_20()
    ): TripLegSection {
        return TripLegSection(
            id = id.toString(),
            departure = departure,
            arrival = arrival,
            distanceInMeters = 100.0
        )
    }
}