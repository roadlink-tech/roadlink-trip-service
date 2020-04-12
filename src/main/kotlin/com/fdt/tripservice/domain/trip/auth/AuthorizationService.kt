package com.fdt.tripservice.domain.trip.auth

import com.fdt.tripservice.domain.trip.auth.Role.ADMIN
import com.fdt.tripservice.domain.trip.auth.Role.DRIVER

open class AuthorizationService {

    open fun canCreateTripWith(tokenInfo: TokenInfo, creatorId: Long): Boolean {
        if (!hasCreatorRole(tokenInfo.roles)) {
            return false
        }
        /*
        * admin users can create trip to another user
        * */
        if (isDifferentCaller(tokenInfo.userId, creatorId)) {
            return tokenInfo.roles.contains(ADMIN)
        }
        return true
    }

    companion object {
        fun isDifferentCaller(callerId: Long, creatorId: Long): Boolean {
            return callerId != creatorId
        }

        fun hasCreatorRole(roles: List<Role>): Boolean {
            return tripCreatorRoles().intersect(roles).isNotEmpty()
        }

        private fun tripCreatorRoles(): List<Role> {
            return listOf(DRIVER, ADMIN)
        }
    }

}
