package com.fdt.tripservice.domain.usecases

import com.fdt.tripservice.application.dto.TripDto
import com.fdt.tripservice.domain.trip.Location
import com.fdt.tripservice.domain.trip.TripFactory
import com.fdt.tripservice.domain.trip.TripRepository
import com.fdt.tripservice.domain.trip.auth.TripAuthService
import com.fdt.tripservice.domain.trip.auth.UnauthorizedException
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.atLeastOnce
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations.initMocks
import java.time.LocalDate

class CreateTripTest {

    @Mock
    private lateinit var tripFactory: TripFactory

    @Mock
    private lateinit var tripAuthService: TripAuthService

    @Mock
    private lateinit var tripRepository: TripRepository

    private lateinit var createTrip: CreateTrip
    //Passenger
    private val noCreatorId = 1L
    private val tokenWithoutCreatorRoles = "asldasd"
    //Driver
    private val driverId = 2L
    private val tokenWithDriverRole = "akfñwe"
    //Admin
    private val adminId = 3L
    private val tokenWithAdminRole = "asflkjsdgjngbk"


    @BeforeEach
    fun setUp() {
        initMocks(this)
        createTrip = CreateTrip(tripAuthService, tripFactory, tripRepository)
        //Passenger
        `when`(tripAuthService.verifyCreatorPermissionFor(tokenWithoutCreatorRoles, noCreatorId))
                .thenThrow(UnauthorizedException::class.java)
    }

    @Test
    fun `when user does not have driver role then can't create any trip`() {
        //GIVEN
        val tripDto = givenAnyTripDto()

        //THEN
        assertThrows<UnauthorizedException> { createTrip.execute(tokenWithoutCreatorRoles, tripDto) }
    }

    @Test
    fun `when user has driver role then can create trips`() {
        //GIVEN
        val tripDto = givenAnyTripDto()

        //WHEN
        createTrip.execute(tokenWithDriverRole, tripDto)

        //THEN
        noExceptionWasThrown()
        tripWasSaved()
    }

    @Test
    fun `when user has admin role then can create trips`() {
        //GIVEN
        val tripDto = givenAnyTripDto()

        //WHEN
        createTrip.execute(tokenWithAdminRole, tripDto)

        //THEN
        noExceptionWasThrown()
        tripWasSaved()
    }

    private fun givenAnyTripDto() =
            TripDto(Location(0L, 0L), Location(1L, 1L), LocalDate.now(), noCreatorId, listOf(), 1)

    private fun tripWasSaved() {
        verify(tripRepository, atLeastOnce()).save(any())
    }

    private fun noExceptionWasThrown() {
        //nothing to do
    }
}