package com.roadlink.tripservice.domain.trip_solicitude

import com.roadlink.tripservice.domain.common.DomainError
import com.roadlink.tripservice.domain.common.TripPoint
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude.TripLegSolicitude.Status.*
import com.roadlink.tripservice.domain.trip.section.Section
import java.util.*

data class TripPlanSolicitude(
    val id: UUID = UUID.randomUUID(),
    val tripLegSolicitudes: MutableList<TripLegSolicitude> = mutableListOf()
) {
    // Todo move this to a common class
    enum class Status {
        PENDING_APPROVAL,
        REJECTED,
        CONFIRMED
    }

    /**
     * TripLegSolicitude is a leg of a trip plan grouped by a driver.
     */
    data class TripLegSolicitude(
        val id: UUID = UUID.randomUUID(),
        val sections: List<Section>,
        val passengerId: String,
        var status: Status = PENDING_APPROVAL,
        val authorizerId: String
    ) {
        enum class Status {
            PENDING_APPROVAL,
            REJECTED,
            CONFIRMED
        }

        fun driverId(): UUID {
            return UUID.fromString(this.sections.first().driverId)
        }

        fun tripId(): UUID {
            return this.sections.first().tripId
        }

        fun departureTripPoint(): TripPoint =
            sections.first().departure

        fun arrivalTripPoint(): TripPoint =
            sections.last().arrival

        internal fun isPendingApproval(): Boolean {
            return this.status == PENDING_APPROVAL
        }

        internal fun isConfirmed(): Boolean {
            return this.status == CONFIRMED
        }

        internal fun isRejected(): Boolean {
            return this.status == REJECTED
        }

        internal fun confirm() {
            this.status = CONFIRMED
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

    fun confirmApplicationById(tripApplicationId: UUID, callerId: UUID?) {
        val application = this.tripLegSolicitudes.find { it.id == tripApplicationId }
            ?: throw TripApplicationError.NotFound(tripApplicationId)

        if (callerId != null && isAnyDriverTryingToJoinAsPassenger(callerId)) {
            throw TripApplicationError.DriverTryingToJoinAsPassenger(tripApplicationId, callerId)
        }

        application.sections.forEach { section ->
            section.takeSeat()
        }
        application.confirm()
    }

    private fun isAnyDriverTryingToJoinAsPassenger(callerId: UUID): Boolean {
        return this.tripLegSolicitudes.any { it.driverId() == callerId }
    }

    fun isRejected(): Boolean {
        return this.tripLegSolicitudes.any { it.isRejected() }
    }

    private fun isConfirmed(): Boolean {
        return this.tripLegSolicitudes.all { it.isConfirmed() }
    }

    private fun isPendingApproval(): Boolean {
        return !this.tripLegSolicitudes.any { it.isRejected() } && this.tripLegSolicitudes.any { it.isPendingApproval() }
    }

    fun include(application: TripLegSolicitude) {
        this.tripLegSolicitudes.add(application)
    }

    fun status(): Status {
        if (isConfirmed()) {
            return Status.CONFIRMED
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
}

sealed class TripPlanSolicitudeError(message: String) : DomainError(message) {

    class CanNotDeterminateStatus(id: UUID) :
        TripPlanSolicitudeError("Can not determinate status for trip plan application $id ")
}


sealed class TripApplicationError(message: String) : DomainError(message) {
    class NotFound(application: UUID) : TripApplicationError("Trip application $application does not exist")
    class DriverTryingToJoinAsPassenger(application: UUID, driverId: UUID) :
        TripApplicationError("User $driverId is driver and is trying to join to $application trip application as passenger too")
}

