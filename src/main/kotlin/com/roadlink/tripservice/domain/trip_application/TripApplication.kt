package com.roadlink.tripservice.domain.trip_application

import com.roadlink.tripservice.domain.common.DomainError
import com.roadlink.tripservice.domain.common.TripPoint
import com.roadlink.tripservice.domain.trip_application.TripPlanApplication.TripApplication.Status.*
import com.roadlink.tripservice.domain.trip.section.Section
import java.lang.RuntimeException
import java.util.*

/**
 * A trip plan cam contain more than one trip application, because you can take a trip with more than one driver.
 * */
data class TripPlanApplication(
    val id: UUID = UUID.randomUUID(),
    val tripApplications: MutableList<TripApplication> = mutableListOf()
) {
    // Todo move this to a common class
    enum class Status {
        PENDING_APPROVAL,
        REJECTED,
        CONFIRMED
    }

    data class TripApplication(
        val id: UUID = UUID.randomUUID(),
        val sections: List<Section>,
        val passengerId: String,
        var status: Status = PENDING_APPROVAL,
        // TODO qué es esto?
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
        val application = this.tripApplications.find { it.id == tripApplicationId }
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
        return this.tripApplications.any { it.driverId() == callerId }
    }

    fun isRejected(): Boolean {
        return this.tripApplications.any { it.isRejected() }
    }

    private fun isConfirmed(): Boolean {
        return this.tripApplications.all { it.isConfirmed() }
    }

    private fun isPendingApproval(): Boolean {
        return !this.tripApplications.any { it.isRejected() } && this.tripApplications.any { it.isPendingApproval() }
    }

    fun include(application: TripApplication) {
        this.tripApplications.add(application)
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
        throw TripPlanApplicationError.CanNotDeterminateStatus(this.id)
    }

    fun reject() {
        if (!this.isRejected()) {
            tripApplications.forEach { tripApplication ->
                tripApplication.reject()
            }
        }
    }
}

sealed class TripPlanApplicationError(message: String) : DomainError(message) {

    class CanNotDeterminateStatus(id: UUID) :
        TripPlanApplicationError("Can not determinate status for trip plan application $id ")
}


sealed class TripApplicationError(message: String) : DomainError(message) {
    class NotFound(application: UUID) : TripApplicationError("Trip application $application does not exist")
    class DriverTryingToJoinAsPassenger(application: UUID, driverId: UUID) :
        TripApplicationError("User $driverId is driver and is trying to join to $application trip application as passenger too")
}

