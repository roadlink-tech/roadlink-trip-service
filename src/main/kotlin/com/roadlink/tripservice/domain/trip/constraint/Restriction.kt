package com.roadlink.tripservice.domain.trip.constraint

import com.roadlink.tripservice.domain.trip_search.Filter
import com.roadlink.tripservice.domain.trip.Trip
import com.roadlink.tripservice.domain.user.User

/**
 * Interface defining a restriction applicable to a trip.
 *
 * This interface is intended to impose specific limitations or conditions on trips
 * with respect to different types of users. Implementations of this interface
 * determine whether a trip is accessible or suitable for a user based on various
 * criteria, such as trip visibility, or regulatory compliance.
 *
 */
interface Restriction : Constraint {
    /**
     * Determines whether a trip can be selected by a specific user based on the restriction.
     *
     * This method assesses whether the given trip complies with the restrictions necessary
     * for the requester. It returns `true` if the trip is permissible for the user,
     * and `false` otherwise.
     *
     * @param requester The user for whom the trip's suitability is being checked.
     * @param trip The trip being evaluated against the restriction.
     * @return `true` if the trip is allowed for the requester, `false` otherwise.
     */
    fun isAllowed(requester: User, trip: Trip): Boolean
}

sealed class Visibility : Restriction {
    object Private : Visibility() {
        override fun isAllowed(requester: User, trip: Trip): Boolean {
            return true
        }
    }

    object FriendsOfFriends : Visibility() {
        override fun isAllowed(requester: User, trip: Trip): Boolean {
            return true
        }
    }

    companion object {
        fun valueOf(filter: Filter): Visibility? {
            return when (filter) {
                Filter.PRIVATE -> Private
                Filter.FRIENDS_OF_FRIENDS -> FriendsOfFriends
                else -> null

            }
        }
    }
}