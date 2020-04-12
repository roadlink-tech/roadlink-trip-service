package com.fdt.tripservice.domain.usecases

import com.fdt.tripservice.domain.trip.Location
import com.fdt.tripservice.domain.trip.Subtrip
import com.fdt.tripservice.domain.trip.Trip
import com.fdt.tripservice.domain.trip.TripRepository
import com.fdt.tripservice.domain.trip.auth.TripAuthService
import com.fdt.tripservice.domain.trip.auth.UnauthorizedException
import com.fdt.tripservice.domain.trip.exception.InvalidTripSectionException
import com.fdt.tripservice.domain.trip.exception.TripNotFoundException
import com.fdt.tripservice.domain.trip.exception.UnavailableTripSeatException
import com.fdt.tripservice.domain.trip.exception.UserAlreadyAddedToTripException
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
    private val otherPassengerId = 3L
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
    fun `when passenger try to join to an not existing trip then must fail`() {
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

    @Test
    fun `when user with required permissions try to join an full trip then must fail`() {
        givenATripIsFull()

        Assertions.assertThrows(UnavailableTripSeatException::class.java) {
            joinTrip.execute(passengerToken, validTripId, validSubtrip, passengerId)
        }
    }

    @Test
    fun `when user with required permissions try to join a not existing section in the trip then must fail`() {
        givenAnyTrip()

        val subtrip = Subtrip(locNotInTrip(), otherLocNotInTrip())
        Assertions.assertThrows(InvalidTripSectionException::class.java) {
            joinTrip.execute(passengerToken, validTripId, subtrip, passengerId)
        }
    }

    @Test
    fun `when user with required permissions try to join a section with not existing initial location in the trip then must fail`() {
        val trip = givenAnyTrip()

        val subtrip = Subtrip(locNotInTrip(), trip.arrival)
        Assertions.assertThrows(InvalidTripSectionException::class.java) {
            joinTrip.execute(passengerToken, validTripId, subtrip, passengerId)
        }
    }

    @Test
    fun `when user with required permissions try to join a section with not existing final location in the trip then must fail`() {
        val trip = givenAnyTrip()

        val subtrip = Subtrip(trip.departure, locNotInTrip())
        Assertions.assertThrows(InvalidTripSectionException::class.java) {
            joinTrip.execute(passengerToken, validTripId, subtrip, passengerId)
        }
    }

    @Test
    fun `when user with required permissions try to join again to a entire trip then must fail`() {
        val trip = givenAnyTrip()

        val entireTrip = Subtrip(trip.departure, trip.arrival)
        joinTrip.execute(passengerToken, validTripId, entireTrip, passengerId)

        Assertions.assertThrows(UserAlreadyAddedToTripException::class.java) {
            joinTrip.execute(passengerToken, validTripId, entireTrip, passengerId)
        }
    }

    @Test
    fun `when user with required permissions try to join another section of the same trip then must fail`() {
        givenAnyTrip()

        val subtripCD = Subtrip(locC(), locD())
        joinTrip.execute(passengerToken, validTripId, subtripCD, passengerId)

        val subtripBC = Subtrip(locB(), locC())
        Assertions.assertThrows(UserAlreadyAddedToTripException::class.java) {
            joinTrip.execute(passengerToken, validTripId, subtripBC, passengerId)
        }
    }



    private fun givenATripIsFull() {
        val trip = anyTrip(1)
        `when`(tripRepository.findById(validTripId)).thenReturn(trip)
        trip.joinPassengerAt(otherPassengerId, Subtrip(trip.departure, trip.arrival))
    }

    private fun givenAnyTrip(): Trip {
        val trip = anyTrip()
        `when`(tripRepository.findById(validTripId)).thenReturn(trip)
        return trip
    }

    private fun anyTrip(capacity: Int = 4): Trip {
        val locA = locA()
        val locB = locB()
        val locC = locC()
        val locD = locD()
        return Trip(validTripId, locA, locD, LocalDate.now(), listOf(locB, locC), 1L, capacity)
    }

    private fun locD() = Location(3L, 3L)

    private fun locC() = Location(2L, 2L)

    private fun locB() = Location(1L, 1L)

    private fun locA() = Location(0L, 0L)

    private fun locNotInTrip() = Location(4L, 3L)

    private fun otherLocNotInTrip() = Location(1L, 2L)

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