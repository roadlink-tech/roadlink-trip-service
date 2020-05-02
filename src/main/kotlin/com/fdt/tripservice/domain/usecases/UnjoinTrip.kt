package com.fdt.tripservice.domain.usecases

import com.fdt.tripservice.domain.trip.TripRepository
import com.fdt.tripservice.domain.trip.auth.TripAuthService

class UnjoinTrip(
        private val tripAuthService: TripAuthService,
        private val tripRepository: TripRepository
) {
    fun execute(token: String, tripId: Long, passengerId: Long) {
        tripAuthService.verifyUnjoinerPermissionFor(token, passengerId)

        val trip = tripRepository.findById(tripId)
        trip.unjoinPassenger(passengerId)
        tripRepository.save(trip)
    }
}