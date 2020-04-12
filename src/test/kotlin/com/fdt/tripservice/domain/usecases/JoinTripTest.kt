package com.fdt.tripservice.domain.usecases

import com.fdt.tripservice.domain.trip.Location
import com.fdt.tripservice.domain.trip.Subtrip
import com.fdt.tripservice.domain.trip.Trip
import com.fdt.tripservice.domain.trip.TripRepository
import com.fdt.tripservice.domain.trip.auth.TripAuthService
import com.fdt.tripservice.domain.trip.auth.UnauthorizedException
import com.fdt.tripservice.domain.trip.exception.TripNotFoundException
import com.nhaarman.mockitokotlin2.atLeastOnce
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations.initMocks
import java.time.LocalDate

class JoinTripTest {

    @Mock
    private lateinit var tripAuthService: TripAuthService

    @Mock
    private lateinit var tripRepository: TripRepository

    private lateinit var joinTrip: JoinTrip

    private val passengerToken = "aflsjglkfdjlkgjfd"
    private val driverToken = "gsldksglkfsglkfgldk"
    private val passengerId = 1L
    private val driverId = 2L
    private val validTripId = 1L
    private val invalidTrip = 9999999999L
    private val validSubtrip = Subtrip(Location(0L, 0L), Location(1L, 1L))

    private val expectedTrip = Trip(
            validTripId,
            Location(0L, 0L),
            Location(1L, 1L),
            LocalDate.now(),
            emptyList(),
            driverId,
            1)

    @BeforeEach
    fun setUp() {
        initMocks(this)
        joinTrip = JoinTrip(tripAuthService, tripRepository)
    }

    @Test
    fun `when passenger try to join trip with available seats then should work ok`() {
        //GIVEN
        findExpectedTrip()

        //WHEN
        joinTrip.execute(passengerToken, validTripId, validSubtrip, passengerId)

        //THEN
        tripWasUpdated()
        noExceptionWasThrown()
    }

    @Test
    fun `when passenger try to join to an invalid trip then must fail`() {
        //GIVEN
        thrownTripNotFoundException()

        //WHEN
        Assertions.assertThrows(TripNotFoundException::class.java) {
            joinTrip.execute(passengerToken, invalidTrip, validSubtrip, passengerId)
        }
    }

    @Test
    fun `when user without required permission try to join to trip then must fail`() {
        //GIVEN
        findExpectedTrip()
        thrownExceptionGivenUserWithoutPermission()

        //THEN
        Assertions.assertThrows(UnauthorizedException::class.java) {
            joinTrip.execute(driverToken, validTripId, validSubtrip, driverId)
        }
    }

    private fun thrownExceptionGivenUserWithoutPermission() {
        `when`(tripAuthService.verifyJoinerPermissionFor(driverToken, driverId))
                .thenThrow(UnauthorizedException::class.java)
    }

    private fun findExpectedTrip() {
        `when`(tripRepository.findById(validTripId)).thenReturn(expectedTrip)
    }

    private fun noExceptionWasThrown() {
        //nothing to do
    }

    private fun tripWasUpdated() {
        verify(tripRepository, atLeastOnce()).save(expectedTrip)
    }

    private fun thrownTripNotFoundException() {
        `when`(tripRepository.findById(invalidTrip)).thenThrow(TripNotFoundException::class.java)
    }
}