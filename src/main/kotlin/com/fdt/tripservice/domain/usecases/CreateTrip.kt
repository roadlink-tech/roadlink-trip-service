package com.fdt.tripservice.domain.usecases

import com.fdt.tripservice.application.dto.TripDto
import com.fdt.tripservice.domain.trip.Trip
import com.fdt.tripservice.domain.trip.auth.AuthorizationService
import com.fdt.tripservice.domain.trip.TripFactory
import com.fdt.tripservice.domain.trip.TripRepository
import com.fdt.tripservice.domain.trip.auth.TokenInfo
import com.fdt.tripservice.domain.trip.auth.TokenService
import com.fdt.tripservice.domain.trip.auth.UnauthorizedException

class CreateTrip(
        private val authorizationService: AuthorizationService,
        private val tokenService: TokenService,
        private val tripFactory: TripFactory,
        private val tripRepository: TripRepository) {

    fun execute(token: String, tripDto: TripDto): Trip {
        val tokenInfo = tokenService.getTokenInfo(token)
        verifyPermission(tokenInfo, tripDto.creatorId)
        val trip = tripFactory.create(tripDto)
        return tripRepository.save(trip)
    }

    private fun verifyPermission(tokenInfo: TokenInfo, creatorId: Long) {
        val canCreateTrip = authorizationService.canCreateTripWith(tokenInfo, creatorId)
        if (!canCreateTrip) {
            throw UnauthorizedException("User ${tokenInfo.userId} does not have permission to perform this action.")
        }
    }
}