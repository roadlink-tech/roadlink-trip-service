package com.fdt.tripservice.domain.usecases

import com.fdt.tripservice.application.dto.TripDto
import com.fdt.tripservice.domain.trip.Trip
import com.fdt.tripservice.domain.trip.TripFactory
import com.fdt.tripservice.domain.trip.TripRepository
import com.fdt.tripservice.domain.trip.auth.TripAuthService

class CreateTrip(
        private val tripAuthService: TripAuthService,
        private val tripFactory: TripFactory,
        private val tripRepository: TripRepository) {

    fun execute(token: String, tripDto: TripDto): Trip {
        tripAuthService.verifyCreatorPermissionFor(token, tripDto.creatorId)
        val trip = tripFactory.create(tripDto)
        return tripRepository.save(trip)
    }

}