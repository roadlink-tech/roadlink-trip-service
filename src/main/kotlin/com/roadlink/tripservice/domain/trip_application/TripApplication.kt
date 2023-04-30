package com.roadlink.tripservice.domain.trip_application

import com.roadlink.tripservice.domain.trip_application.TripPlanApplication.TripApplication.Status.*
import com.roadlink.tripservice.domain.trip.section.Section
import java.util.*

data class TripPlanApplication(
    val id: UUID = UUID.randomUUID(),
    val tripApplications: MutableList<TripApplication> = mutableListOf()
) {
    data class TripApplication(
        val id: UUID = UUID.randomUUID(),
        val sections: Set<Section>,
        val passengerId: String,
        var status: Status = PENDING_APPROVAL,
        val authorizerId: String
    ) {
        enum class Status {
            PENDING_APPROVAL,
            REJECTED,
            CONFIRMED
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
                section.releaseSeat()
            }
            status = REJECTED
        }
    }

    fun isReadyToBeConfirm(): Boolean {
        return this.tripApplications.filter { it.isConfirmed() }.size == this.tripApplications.size
    }


    fun confirmApplicationId(applicationId: UUID) {
        val application = this.tripApplications.first { it.id == applicationId }
        application.sections.forEach { section ->
            section.takeSeat()
        }
        application.confirm()
    }

    fun isRejected(): Boolean {
        return this.tripApplications.any { it.isRejected() }
    }

    fun include(application: TripApplication) {
        this.tripApplications.add(application)
    }

    fun reject() {
        if (!this.isRejected()) {
            tripApplications.forEach { tripApplication ->
                tripApplication.reject()
            }
        }
    }
}

