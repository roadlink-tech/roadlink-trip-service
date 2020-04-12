package com.fdt.tripservice.domain.trip.auth

import com.fdt.tripservice.domain.trip.auth.Role.ADMIN
import com.fdt.tripservice.domain.trip.auth.Role.DRIVER

open class TripAuthService(private val authRepository: AuthRepository) {

    open fun verifyCreatorPermissionWith(token: String, creatorId: Long) {
        val auth = authRepository.findByToken(token)
        val canCreateTrip = canCreateTripWith(auth, creatorId)
        if (!canCreateTrip) {
            throw UnauthorizedException("User ${auth.userId} does not have permission to perform this action.")
        }
    }

    open fun canCreateTripWith(auth: Auth, creatorId: Long): Boolean {
        if (!hasCreatorRole(auth.roles)) {
            return false
        }
        /*
        * admin users can create trip to another user
        * */
        if (isDifferentCaller(auth.userId, creatorId)) {
            return auth.roles.contains(ADMIN)
        }
        return true
    }

    private fun isDifferentCaller(callerId: Long, creatorId: Long): Boolean {
        return callerId != creatorId
    }

    private fun hasCreatorRole(roles: List<Role>): Boolean {
        return tripCreatorRoles().intersect(roles).isNotEmpty()
    }

    private fun tripCreatorRoles(): List<Role> {
        return listOf(DRIVER, ADMIN)
    }
}
