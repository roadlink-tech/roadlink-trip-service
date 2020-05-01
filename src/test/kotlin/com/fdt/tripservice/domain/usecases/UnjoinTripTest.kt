package com.fdt.tripservice.domain.usecases

import com.fdt.tripservice.domain.trip.Trip
import com.fdt.tripservice.domain.trip.TripRepository
import com.fdt.tripservice.domain.trip.auth.TripAuthService
import com.fdt.tripservice.domain.trip.auth.UnauthorizedException
import com.fdt.tripservice.domain.trip.exception.TripNotFoundException
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito

class UnjoinTripTest {

    private lateinit var tripAuthService: TripAuthService
    private lateinit var tripRepository: TripRepository
    private lateinit var unjoinTrip: UnjoinTrip

    @BeforeEach
    fun setUp() {
        tripAuthService = Mockito.mock(TripAuthService::class.java)
        tripRepository = Mockito.mock(TripRepository::class.java)
        unjoinTrip = UnjoinTrip(tripAuthService, tripRepository)
    }

    @Test
    fun `given a trip not exists when passenger unjoin it then must fail`() {
        val tripId = 1L
        givenTripNotExists(tripId)
        val passengerId = 2L
        val passengerToken = givenAValidTokenFor(passengerId)

        assertThrows<TripNotFoundException> {
            unjoinTrip.execute(passengerToken, tripId, passengerId)
        }
    }

    @Test
    fun `given a trip exists when passenger unjoin trip then the trip is updated`() {
        val tripId = 1L
        val trip = givenTripExists(tripId)
        val passengerId = 2L
        val passengerToken = givenAValidTokenFor(passengerId)

        unjoinTrip.execute(passengerToken, tripId, passengerId)

        thenTripWasUpdated(trip)
    }

    @Test
    fun `when user without required permission try to unjoin a trip then must fail`() {
        val tripId = 1L
        givenTripExists(tripId)
        val passengerId = 2L
        val invalidToken = givenATokenWithoutPermissionFor(passengerId)

        assertThrows<UnauthorizedException> {
            unjoinTrip.execute(invalidToken, tripId, passengerId)
        }
    }

    private fun givenATokenWithoutPermissionFor(userId: Long): String {
        val token = "gsldksglkfsglkfgldk"
        Mockito.`when`(tripAuthService.verifyUnjoinerPermissionFor(token, userId))
                .thenThrow(UnauthorizedException::class.java)
        return token
    }

    private fun givenAValidTokenFor(userId: Long): String {
        val validToken = "aflsjglkfdjlkgjfd"
        return validToken
    }

    private fun givenTripNotExists(tripId: Long) {
        Mockito.`when`(tripRepository.findById(tripId)).thenThrow(TripNotFoundException(""))
    }

    private fun givenTripExists(tripId: Long): Trip {
        val trip = Mockito.mock(Trip::class.java)
        Mockito.`when`(tripRepository.findById(tripId)).thenReturn(trip)
        return trip
    }

    private fun thenTripWasUpdated(trip: Trip) {
        verify(tripRepository).save(trip)
    }
}