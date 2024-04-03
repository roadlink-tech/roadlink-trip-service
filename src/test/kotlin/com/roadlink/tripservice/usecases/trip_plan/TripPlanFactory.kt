package com.roadlink.tripservice.usecases.trip_plan

import com.roadlink.tripservice.domain.trip_plan.TripLegSection
import com.roadlink.tripservice.domain.trip_plan.TripPlan
import com.roadlink.tripservice.domain.trip_plan.TripPlan.Status
import com.roadlink.tripservice.domain.trip_plan.TripPlan.TripLeg
import java.util.*

object TripPlanFactory {

    fun withASingleTripLeg(
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

    fun common(
        id: UUID = UUID.randomUUID(),
        passengerId: UUID = UUID.randomUUID(),
        tripLegs: List<TripLeg> = listOf(
            TripLeg(
                id = UUID.randomUUID(),
                tripId = UUID.randomUUID(),
                vehicleId = UUID.randomUUID(),
                driverId = UUID.randomUUID(),
                sections = listOf(TripLegSectionFactory.common()),
                status = Status.NOT_FINISHED
            )
        ),
    ): TripPlan {
        return TripPlan(
            id = id,
            passengerId = passengerId,
            tripLegs = tripLegs
        )
    }

    fun withTwoTripLeg(
        id: UUID = UUID.randomUUID(),
        passengerId: UUID = UUID.randomUUID(),
        oneTripLegId: UUID = UUID.randomUUID(),
        anotherTripLegId: UUID = UUID.randomUUID(),
        oneTripId: UUID = UUID.randomUUID(),
        anotherTripId: UUID = UUID.randomUUID(),
        oneDriverId: UUID = UUID.randomUUID(),
        anotherDriverId: UUID = UUID.randomUUID(),
        oneVehicleId: UUID = UUID.randomUUID(),
        anotherVehicleId: UUID = UUID.randomUUID(),
        status: Status = Status.NOT_FINISHED
    ): TripPlan {
        return TripPlan(
            id = id,
            passengerId = passengerId,
            tripLegs = listOf(
                TripLeg(
                    id = oneTripLegId,
                    tripId = oneTripId,
                    vehicleId = oneVehicleId,
                    driverId = oneDriverId,
                    sections = emptyList(),
                    status = status
                ),
                TripLeg(
                    id = anotherTripLegId,
                    tripId = anotherTripId,
                    vehicleId = anotherVehicleId,
                    driverId = anotherDriverId,
                    sections = emptyList(),
                    status = status
                ),
            )
        )
    }
}