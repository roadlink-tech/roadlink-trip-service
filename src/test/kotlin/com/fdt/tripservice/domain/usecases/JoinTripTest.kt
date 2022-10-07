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
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations.initMocks
import java.time.LocalDate
/*
class JoinTripTest {

    @Mock
    private lateinit var tripAuthService: TripAuthService

    @Mock
    private lateinit var tripRepository: TripRepository

    private lateinit var joinTrip: JoinTrip


    @BeforeEach
    fun setUp() {
        initMocks(this)
        joinTrip = JoinTrip(tripAuthService, tripRepository)
    }

    @Test
    fun `when passenger try to join trip with available seats then should work ok`() {
        //GIVEN
        val passengerId = 1L
        val passengerToken = givenAValidTokenFor(passengerId)
        val tripWithAvailableSeats = givenATripWithAvailableSeats()
        val validSubtrip = givenAValidSubtrip()

        //WHEN
        joinTrip.execute(passengerToken, tripWithAvailableSeats.id!!, validSubtrip, passengerId)

        //THEN
        thenTripWasUpdated(tripWithAvailableSeats)
        noExceptionWasThrown()
    }

    @Test
    fun `when passenger try to join to an not existing trip then must fail`() {
        //GIVEN
        val passengerId = 1L
        val passengerToken = givenAValidTokenFor(passengerId)
        val notExistingTripId = givenANotExistingTrip()
        val validSubtrip = givenAValidSubtrip()

        //WHEN
        Assertions.assertThrows(TripNotFoundException::class.java) {
            joinTrip.execute(passengerToken, notExistingTripId, validSubtrip, passengerId)
        }
    }

    @Test
    fun `when user without required permission try to join to trip then must fail`() {
        //GIVEN
        val driverId = 1L
        val tripWithAvailableSeats = givenATripWithAvailableSeats()
        val tokenWithoutPermissions = givenATokenWithoutPermissionFor(driverId)
        val validSubtrip = givenAValidSubtrip()

        //THEN
        Assertions.assertThrows(UnauthorizedException::class.java) {
            joinTrip.execute(tokenWithoutPermissions, tripWithAvailableSeats.id!!, validSubtrip, driverId)
        }
    }

    @Test
    fun `when user with required permissions try to join an full trip then must fail`() {
        val passengerId = 1L
        val passengerToken = givenAValidTokenFor(passengerId)
        val tripFull = givenATripIsFull()
        val validSubtrip = givenAValidSubtrip()

        Assertions.assertThrows(UnavailableTripSeatException::class.java) {
            joinTrip.execute(passengerToken, tripFull.id!!, validSubtrip, passengerId)
        }
    }

    @Test
    fun `when user with required permissions try to join a not existing section in the trip then must fail`() {
        val passengerId = 1L
        val passengerToken = givenAValidTokenFor(passengerId)
        val trip = givenATripWithAvailableSeats()

        val subtrip = Subtrip(locNotInTrip(), otherLocNotInTrip())
        Assertions.assertThrows(InvalidTripSectionException::class.java) {
            joinTrip.execute(passengerToken, trip.id!!, subtrip, passengerId)
        }
    }

    @Test
    fun `when user with required permissions try to join a section with not existing initial location in the trip then must fail`() {
        val passengerId = 1L
        val passengerToken = givenAValidTokenFor(passengerId)
        val trip = givenATripWithAvailableSeats()

        val subtrip = Subtrip(locNotInTrip(), trip.arrival)
        Assertions.assertThrows(InvalidTripSectionException::class.java) {
            joinTrip.execute(passengerToken, trip.id!!, subtrip, passengerId)
        }
    }

    @Test
    fun `when user with required permissions try to join a section with not existing final location in the trip then must fail`() {
        val passengerId = 1L
        val passengerToken = givenAValidTokenFor(passengerId)
        val trip = givenATripWithAvailableSeats()

        val subtrip = Subtrip(trip.departure, locNotInTrip())
        Assertions.assertThrows(InvalidTripSectionException::class.java) {
            joinTrip.execute(passengerToken, trip.id!!, subtrip, passengerId)
        }
    }

    @Test
    fun `when user with required permissions try to join again to a entire trip then must fail`() {
        val passengerId = 1L
        val passengerToken = givenAValidTokenFor(passengerId)
        val trip = givenATripWithAvailableSeats()

        val entireTrip = Subtrip(trip.departure, trip.arrival)
        joinTrip.execute(passengerToken, trip.id!!, entireTrip, passengerId)

        Assertions.assertThrows(UserAlreadyAddedToTripException::class.java) {
            joinTrip.execute(passengerToken, trip.id!!, entireTrip, passengerId)
        }
    }

    @Test
    fun `when user with required permissions try to join another section of the same trip then must fail`() {
        val passengerId = 1L
        val passengerToken = givenAValidTokenFor(passengerId)
        val trip = givenATripWithAvailableSeats()

        val subtripCD = Subtrip(locC(), locD())
        joinTrip.execute(passengerToken, trip.id!!, subtripCD, passengerId)

        val subtripBC = Subtrip(locB(), locC())
        Assertions.assertThrows(UserAlreadyAddedToTripException::class.java) {
            joinTrip.execute(passengerToken, trip.id!!, subtripBC, passengerId)
        }
    }

    private fun givenATripWithAvailableSeats(): Trip {
        val trip = anyTrip()
        `when`(tripRepository.findById(trip.id!!)).thenReturn(trip)
        return trip
    }

    private fun givenATripIsFull(): Trip {
        val trip = anyTrip(1)
        `when`(tripRepository.findById(trip.id!!)).thenReturn(trip)
        trip.joinPassengerAt(99L, Subtrip(trip.departure, trip.arrival))
        return trip
    }

    private fun givenANotExistingTrip(): Long {
        val invalidTripId = 1L
        `when`(tripRepository.findById(invalidTripId)).thenThrow(TripNotFoundException::class.java)
        return invalidTripId
    }

    private fun anyTrip(capacity: Int = 4): Trip {
        val locA = locA()
        val locB = locB()
        val locC = locC()
        val locD = locD()
        return Trip(1L, locA, locD, LocalDate.now(), listOf(locB, locC), 1L, capacity)
    }

    private fun givenAValidSubtrip() = Subtrip(locA(), locB())

    private fun locD() = Location(3L, 3L)

    private fun locC() = Location(2L, 2L)

    private fun locB() = Location(1L, 1L)

    private fun locA() = Location(0L, 0L)

    private fun locNotInTrip() = Location(4L, 3L)

    private fun otherLocNotInTrip() = Location(1L, 2L)

    private fun givenATokenWithoutPermissionFor(userId: Long): String {
        val driverToken = "gsldksglkfsglkfgldk"
        `when`(tripAuthService.verifyJoinerPermissionFor(driverToken, userId))
                .thenThrow(UnauthorizedException::class.java)
        return driverToken
    }

    private fun givenAValidTokenFor(userId: Long): String {
        val validToken = "aflsjglkfdjlkgjfd"
        return validToken
    }

    private fun noExceptionWasThrown() {
        //nothing to do
    }

    private fun thenTripWasUpdated(trip: Trip) {
        verify(tripRepository, atLeastOnce()).save(trip)
    }
}

 */
