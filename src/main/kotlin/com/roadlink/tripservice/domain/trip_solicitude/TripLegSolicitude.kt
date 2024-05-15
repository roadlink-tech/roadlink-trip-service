package com.roadlink.tripservice.domain.trip_solicitude

import com.roadlink.tripservice.domain.common.DomainError
import com.roadlink.tripservice.domain.common.TripPoint
import com.roadlink.tripservice.domain.trip.section.Section
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude.TripLegSolicitude.Status.*
import java.time.Instant
import java.util.*

data class TripPlanSolicitude(
    val id: UUID = UUID.randomUUID(),
    val tripLegSolicitudes: MutableList<TripLegSolicitude> = mutableListOf()
) {
    init {
        // TODO test me!!!
        tripLegSolicitudes.sortBy { it.departureTime() }
    }

    // Todo move this to a common class
    enum class Status {
        PENDING_APPROVAL,
        REJECTED,
        ACCEPTED
    }

    fun acceptTripLegSolicitude(tripLegSolicitudeId: UUID, callerId: UUID?) {
        val tripLegSolicitude = this.tripLegSolicitudes.find { it.id == tripLegSolicitudeId }
            ?: throw TripLegSolicitudeError.NotFound(tripLegSolicitudeId)

        if (callerId != null && isAnyDriverTryingToJoinAsPassenger(callerId)) {
            throw TripLegSolicitudeError.DriverTryingToJoinAsPassenger(tripLegSolicitudeId, callerId)
        }

        tripLegSolicitude.sections.forEach { section ->
            section.takeSeat()
        }
        tripLegSolicitude.accept()
    }

    fun passengerId(): UUID {
        return UUID.fromString(this.tripLegSolicitudes.first().passengerId)
    }

    private fun isAnyDriverTryingToJoinAsPassenger(callerId: UUID): Boolean {
        return this.tripLegSolicitudes.any { it.driverId() == callerId }
    }

    fun isRejected(): Boolean {
        return this.tripLegSolicitudes.any { it.isRejected() }
    }

    fun isAccepted(): Boolean {
        return this.tripLegSolicitudes.all { it.isAccepted() }
    }

    private fun isPendingApproval(): Boolean {
        return !this.tripLegSolicitudes.any { it.isRejected() } && this.tripLegSolicitudes.any { it.isPendingApproval() }
    }

    fun include(solicitude: TripLegSolicitude) {
        this.tripLegSolicitudes.add(solicitude)
    }

    fun status(): Status {
        if (isAccepted()) {
            return Status.ACCEPTED
        }
        if (isRejected()) {
            return Status.REJECTED
        }
        if (isPendingApproval()) {
            return Status.PENDING_APPROVAL
        }
        throw TripPlanSolicitudeError.CanNotDeterminateStatus(this.id)
    }

    fun reject() {
        if (!this.isRejected()) {
            tripLegSolicitudes.forEach { tripApplication ->
                tripApplication.reject()
            }
        }
    }

    /**
     * TripLegSolicitude is a leg of a trip plan grouped by a driver.
     */
    data class TripLegSolicitude(
        val id: UUID = UUID.randomUUID(),
        var sections: List<Section>,
        // TODO maybe it must be a trip plan attribute
        val passengerId: String,
        var status: Status = PENDING_APPROVAL,
        val authorizerId: String
    ) {
        init {
            sections = sections.sortedBy { it.departure.estimatedArrivalTime }
        }

        fun departureTime(): Instant {
            return this.sections.first().departure.estimatedArrivalTime
        }

        // TODO validar que las secciones tengan un unico driverId, tripId y vehicleId
        enum class Status {
            PENDING_APPROVAL,
            REJECTED,
            ACCEPTED
        }

        fun driverId(): UUID {
            return UUID.fromString(this.sections.first().driverId)
        }

        fun tripId(): UUID {
            return this.sections.first().tripId
        }

        fun vehicleId(): UUID {
            return UUID.fromString(this.sections.first().vehicleId)
        }

        fun departureTripPoint(): TripPoint =
            sections.first().departure

        fun arrivalTripPoint(): TripPoint =
            sections.last().arrival

        internal fun isPendingApproval(): Boolean {
            return this.status == PENDING_APPROVAL
        }

        internal fun isAccepted(): Boolean {
            return this.status == ACCEPTED
        }

        internal fun isRejected(): Boolean {
            return this.status == REJECTED
        }

        internal fun accept() {
            this.status = ACCEPTED
        }

        internal fun reject() {
            sections.forEach { section ->
                if (section.hasAnyBooking()) {
                    section.releaseSeat()
                }
            }
            status = REJECTED
        }
    }
}

sealed class TripPlanSolicitudeError(message: String) : DomainError(message) {

    class CanNotDeterminateStatus(id: UUID) :
        TripPlanSolicitudeError("Can not determinate status for trip plan solicitude $id ")
}


sealed class TripLegSolicitudeError(message: String) : DomainError(message) {
    class NotFound(tripLegSolicitudeId: UUID) :
        TripLegSolicitudeError("Trip leg solicitude $tripLegSolicitudeId does not exist")

    class DriverTryingToJoinAsPassenger(tripLegSolicitudeId: UUID, driverId: UUID) :
        TripLegSolicitudeError("User $driverId is driver and is trying to join to $tripLegSolicitudeId trip leg as passenger too")
}

