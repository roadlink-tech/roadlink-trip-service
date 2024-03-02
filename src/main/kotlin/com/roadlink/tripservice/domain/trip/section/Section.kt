package com.roadlink.tripservice.domain.trip.section

import com.roadlink.tripservice.domain.common.DomainError
import com.roadlink.tripservice.domain.common.Location
import com.roadlink.tripservice.domain.common.TripPoint
import java.time.Instant
import java.util.UUID

data class Section(
    val id: String,
    val tripId: UUID,
    val departure: TripPoint,
    val arrival: TripPoint,
    val distanceInMeters: Double,
    val driverId: String,
    val vehicleId: String,
    var initialAmountOfSeats: Int,
    var bookedSeats: Int
) {

    fun departure(): Location = departure.address.location

    fun arrival(): Location = arrival.address.location

    fun arrivesTo(location: Location): Boolean {
        return arrival.address.location == location
    }

    fun departuresFrom(location: Location): Boolean {
        return departure.address.location == location
    }

    fun distance(): Double = distanceInMeters

    fun departuresAfterOrEqual(at: Instant): Boolean {
        return departure.estimatedArrivalTime.isAfter(at) || departure.estimatedArrivalTime == at
    }

    fun availableSeats(): Int {
        return initialAmountOfSeats - bookedSeats
    }

    fun occupiedSeats(): Int {
        return bookedSeats
    }

    fun canReceiveAnyPassenger(): Boolean {
        return availableSeats() > 0
    }

    fun hasAnyBooking(): Boolean {
        return this.bookedSeats > 0
    }

    fun hasNoSeatsAvailable(): Boolean {
        return availableSeats() == 0
    }

    fun hasAllSeatsAvailable(): Boolean  {
        return availableSeats() == initialAmountOfSeats
    }

    fun releaseSeat() {
        if (hasAllSeatsAvailable()) {
            throw SectionError.ImpossibleToReleaseASeat(this.id)
        }
        bookedSeats -= 1
    }

    fun takeSeat() {
        if (hasNoSeatsAvailable()) {
            throw SectionError.InsufficientAvailableSeats(this.id)
        }
        bookedSeats += 1
    }
}


sealed class SectionError(message: String) : DomainError(message) {
    class InsufficientAvailableSeats(id: String) : SectionError("Section $id does not have available seats")
    class ImpossibleToReleaseASeat(id: String) :
        SectionError("Can not release a seat for section $id, because there aren't any booked seat")
}
