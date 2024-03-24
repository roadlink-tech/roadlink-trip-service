package com.roadlink.tripservice.domain.trip_plan

import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude
import java.util.*

class TripPlan(
    val id: UUID,
    val tripLegs: List<TripLeg>,
    val passengerId: UUID,
) {
    enum class Status {
        FINISHED,
        NOT_FINISHED,
        CANCELLED
    }

    class TripLeg(
        val id: UUID,
        val tripId: UUID,
        val driverId: UUID,
        val vehicleId: UUID,
        val sections: List<TripLegSection>,
        var status: Status,
    ) {
        fun isCancelled(): Boolean {
            return status == Status.CANCELLED
        }

        fun isFinished(): Boolean {
            return status == Status.FINISHED
        }

        fun finish() {
            status = Status.FINISHED
        }
    }

    fun finishLegByTripId(tripId: UUID) {
        this.tripLegs.first { it.tripId == tripId }.finish()
    }

    fun status(): Status {
        if (isCancelled()) {
            return Status.CANCELLED
        }
        if (isFinished()) {
            return Status.FINISHED
        }
        return Status.NOT_FINISHED
    }

    fun isCancelled(): Boolean {
        return this.tripLegs.any { it.isCancelled() }
    }

    fun isFinished(): Boolean {
        return this.tripLegs.all { it.isFinished() }
    }

    companion object {
        fun from(solicitude: TripPlanSolicitude): TripPlan {
            return TripPlan(
                id = solicitude.id,
                passengerId = solicitude.passengerId(),
                tripLegs = solicitude.tripLegSolicitudes.map {
                    TripLeg(
                        id = it.id,
                        tripId = it.tripId(),
                        driverId = it.driverId(),
                        vehicleId = it.vehicleId(),
                        sections = it.sections.map { section ->
                            TripLegSection(
                                id = section.id,
                                departure = section.departure,
                                arrival = section.arrival,
                                distanceInMeters = section.distanceInMeters
                            )
                        },
                        status = Status.NOT_FINISHED
                    )
                }
            )
        }
    }
}