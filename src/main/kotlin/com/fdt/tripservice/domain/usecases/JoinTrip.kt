package com.fdt.tripservice.domain.usecases

import com.fdt.tripservice.domain.trip.Subtrip
import com.fdt.tripservice.domain.trip.exception.InvalidTripSectionException
import com.fdt.tripservice.domain.trip.Trip
import com.fdt.tripservice.domain.trip.TripRepository
import com.fdt.tripservice.domain.trip.exception.UserAlreadyAddedToTripException
import com.fdt.tripservice.domain.trip.auth.TripAuthService
import com.fdt.tripservice.domain.trip.exception.UnavailableTripSeatException

class JoinTrip(
        private val tripAuthService: TripAuthService,
        private val tripRepository: TripRepository) {

    fun execute(token: String, tripId: Long, subtrip: Subtrip, joinerId: Long) {
        tripAuthService.verifyJoinerPermissionFor(token, joinerId)
        val trip = tripRepository.findById(tripId)
        joinBySections(trip, joinerId, subtrip)
        tripRepository.save(trip)
    }

    private fun joinBySections(trip: Trip, userId: Long, subtrip: Subtrip) {
        if (userId in trip) {
            throw UserAlreadyAddedToTripException("User $userId is already added to $trip trip")
        }
        if (subtrip !in trip) {
            throw InvalidTripSectionException("Section $subtrip does not belong to $trip trip")
        }
        if (!trip.hasAvailableSeatAt(subtrip)) {
            throw UnavailableTripSeatException("Section trip $subtrip has't got any available seats")
        }
        trip.joinPassengerAt(userId, subtrip)
    }
}