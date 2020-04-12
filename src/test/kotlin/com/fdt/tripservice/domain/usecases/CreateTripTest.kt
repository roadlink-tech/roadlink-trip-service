package com.fdt.tripservice.domain.usecases

import com.fdt.tripservice.application.dto.TripDto
import com.fdt.tripservice.domain.trip.Location
import com.fdt.tripservice.domain.trip.TripFactory
import com.fdt.tripservice.domain.trip.TripRepository
import com.fdt.tripservice.domain.trip.auth.AuthorizationService
import com.fdt.tripservice.domain.trip.auth.Role.ADMIN
import com.fdt.tripservice.domain.trip.auth.Role.DRIVER
import com.fdt.tripservice.domain.trip.auth.Role.PASSENGER
import com.fdt.tripservice.domain.trip.auth.TokenInfo
import com.fdt.tripservice.domain.trip.auth.TokenService
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
    private lateinit var tokenService: TokenService

    @Mock
    private lateinit var tripFactory: TripFactory

    @Mock
    private lateinit var authorizationService: AuthorizationService

    @Mock
    private lateinit var tripRepository: TripRepository

    private lateinit var createTrip: CreateTrip
    private val noCreatorId = 1L
    private val tokenWithoutCreatorRoles = "asldasd"
    private val tokenInfoWithoutCreatorRoles = TokenInfo(noCreatorId, listOf(PASSENGER))

    private val driverId = 2L
    private val tokenWithDriverRole = "akfñwe"
    private val tokenInfoWithDriverRole = TokenInfo(driverId, listOf(PASSENGER, DRIVER))

    private val adminId = 3L
    private val tokenWithAdminRole = "asflkjsdgjngbk"
    private val tokenInfoWithAdminRole = TokenInfo(adminId, listOf(PASSENGER, DRIVER, ADMIN))


    @BeforeEach
    fun setUp() {
        initMocks(this)
        createTrip = CreateTrip(authorizationService, tokenService, tripFactory, tripRepository)
        //Passenger
        `when`(tokenService.getTokenInfo(tokenWithoutCreatorRoles)).thenReturn(tokenInfoWithoutCreatorRoles)
        `when`(authorizationService.canCreateTripWith(tokenInfoWithoutCreatorRoles, noCreatorId)).thenReturn(false)

        //Driver
        `when`(tokenService.getTokenInfo(tokenWithDriverRole)).thenReturn(tokenInfoWithDriverRole)
        `when`(authorizationService.canCreateTripWith(tokenInfoWithDriverRole, driverId)).thenReturn(true)

        //Admin
        `when`(tokenService.getTokenInfo(tokenWithAdminRole)).thenReturn(tokenInfoWithAdminRole)
        `when`(authorizationService.canCreateTripWith(tokenInfoWithAdminRole, adminId)).thenReturn(true)
    }

    @Test
    fun `when user does not have driver role then can't create any trip`() {
        //GIVEN
        val tripDto = TripDto(Location(), Location(), LocalDate.now(), noCreatorId, listOf())

        //THEN
        assertThrows<UnauthorizedException> { createTrip.execute(tokenWithoutCreatorRoles, tripDto) }
    }

    @Test
    fun `when user has driver role then can create trips`() {
        //GIVEN
        val tripDto = TripDto(Location(), Location(), LocalDate.now(), driverId, listOf())

        //WHEN
        createTrip.execute(tokenWithDriverRole, tripDto)

        //THEN
        noExceptionWasThrown()
        tripWasSaved()
    }

    @Test
    fun `when user has admin role then can create trips`() {
        //GIVEN
        val tripDto = TripDto(Location(), Location(), LocalDate.now(), adminId, listOf())

        //WHEN
        createTrip.execute(tokenWithAdminRole, tripDto)

        //THEN
        noExceptionWasThrown()
        tripWasSaved()
    }

    private fun tripWasSaved() {
        verify(tripRepository, atLeastOnce()).save(any())
    }

    private fun noExceptionWasThrown() {
        //nothing to do
    }
}