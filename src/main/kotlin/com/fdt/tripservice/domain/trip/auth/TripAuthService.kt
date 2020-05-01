package com.fdt.tripservice.domain.trip.auth

import com.fdt.tripservice.domain.trip.auth.Role.ADMIN
import com.fdt.tripservice.domain.trip.auth.Role.DRIVER
import com.fdt.tripservice.domain.trip.auth.Role.PASSENGER

open class TripAuthService(private val authRepository: AuthRepository) {

    private val tripCreatorRoles = listOf(DRIVER, ADMIN)
    private val tripJoinerRoles = listOf(PASSENGER, ADMIN)
    private val tripUnjoinerRoles = listOf(PASSENGER, ADMIN)

    open fun verifyCreatorPermissionFor(token: String, creatorId: Long) {
        verifyPermission(token, creatorId, tripCreatorRoles)
    }

    open fun verifyJoinerPermissionFor(token: String, joinerId: Long) {
        verifyPermission(token, joinerId, tripJoinerRoles)
    }

    open fun verifyUnjoinerPermissionFor(token: String, unjoinerId: Long) {
        verifyPermission(token, unjoinerId, tripUnjoinerRoles)
    }

    private fun verifyPermission(token: String, userId: Long, requiredRoles: List<Role>) {
        val auth = authRepository.findByToken(token)
        val hasPermission = hasPermission(auth, userId, requiredRoles)
        if (!hasPermission) {
            throw UnauthorizedException("User ${auth.userId} does not have permission to perform this action.")
        }
    }

    private fun hasPermission(auth: Auth, creatorId: Long, requiredRoles: List<Role>): Boolean {
        if (!isAuthorizedCaller(auth, creatorId)) {
            return false
        }
        return requiredRoles.intersect(auth.roles).isNotEmpty()
    }

    private fun isAuthorizedCaller(auth: Auth, creatorId: Long): Boolean {
        if (isAdminCaller(auth)) {
            return true
        }
        return !isDifferentCaller(auth.userId, creatorId)
    }

    private fun isAdminCaller(auth: Auth): Boolean {
        return auth.roles.contains(ADMIN)
    }

    private fun isDifferentCaller(callerId: Long, creatorId: Long): Boolean {
        return callerId != creatorId
    }
}
