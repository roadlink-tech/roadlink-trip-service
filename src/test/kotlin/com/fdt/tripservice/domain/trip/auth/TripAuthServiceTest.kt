package com.fdt.tripservice.domain.trip.auth

import com.fdt.tripservice.domain.trip.auth.Role.ADMIN
import com.fdt.tripservice.domain.trip.auth.Role.DRIVER
import com.fdt.tripservice.domain.trip.auth.Role.PASSENGER
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations.initMocks

class TripAuthServiceTest {

    @Mock
    private lateinit var authRepository: AuthRepository

    private lateinit var tripAuthService: TripAuthService

    private val adminToken = "adminToken"
    private val adminId = 2L
    private val authAdmin = Auth(adminId, listOf(ADMIN))

    private val passengerToken = "fjdsfjsd"
    private val passengerId = 1L
    private val authPassenger = Auth(passengerId, listOf(PASSENGER))

    private val driverToken = "driverToken"
    private val driverId = 3L
    private val authDriver = Auth(driverId, listOf(DRIVER))

    private val otherUserId = 4L

    @BeforeEach
    fun setUp() {
        initMocks(this)
        tripAuthService = TripAuthService(authRepository)
        `when`(authRepository.findByToken(adminToken)).thenReturn(authAdmin)
        `when`(authRepository.findByToken(passengerToken)).thenReturn(authPassenger)
        `when`(authRepository.findByToken(driverToken)).thenReturn(authDriver)
    }

    @Test
    fun `when token belongs to admin user then can create trip for a user without require role`() {
        //WHEN
        tripAuthService.verifyCreatorPermissionFor(adminToken, passengerId)

        //THEN
        noExceptionWasThrown()
    }

    @Test
    fun `when token belongs to passenger user then can't create trip`() {
        assertThrows<UnauthorizedException> {
            tripAuthService.verifyCreatorPermissionFor(passengerToken, passengerId)
        }
    }

    @Test
    fun `when token belongs to driver user then can create trip`() {
        //WHEN
        tripAuthService.verifyCreatorPermissionFor(driverToken, driverId)

        //THEN
        noExceptionWasThrown()
    }

    @Test
    fun `when token belongs to non admin user and try to create trip for a passenger user then should throw exception`() {
        assertThrows<UnauthorizedException> {
            tripAuthService.verifyCreatorPermissionFor(driverToken, passengerId)
        }
    }

    @Test
    fun `when token belongs to non admin user and try to create trip for another user then should throw exception`() {
        assertThrows<UnauthorizedException> {
            tripAuthService.verifyCreatorPermissionFor(driverToken, otherUserId)
        }
    }

    @Test
    fun `when token belongs to admin user then can unjoin a user without required role`() {
        //WHEN
        tripAuthService.verifyUnjoinerPermissionFor(adminToken, driverId)

        //THEN
        noExceptionWasThrown()
    }

    @Test
    fun `when token belongs to passenger user then can unjoin a trip`() {
        //WHEN
        tripAuthService.verifyUnjoinerPermissionFor(passengerToken, passengerId)

        //THEN
        noExceptionWasThrown()
    }

    @Test
    fun `when token belongs to passenger user and try to unjoin another user then must fail`() {
        assertThrows<UnauthorizedException> {
            tripAuthService.verifyCreatorPermissionFor(passengerToken, otherUserId)
        }
    }

    @Test
    fun `when token belongs to driver user user must fail to unjoin a trip`() {
        assertThrows<UnauthorizedException> {
            tripAuthService.verifyCreatorPermissionFor(passengerToken, otherUserId)
        }
    }

    private fun noExceptionWasThrown() {
        //nothing to do
    }
}