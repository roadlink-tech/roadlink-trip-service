package com.roadlink.tripservice.domain.trip.constraint

import com.roadlink.tripservice.domain.trip.Trip
import com.roadlink.tripservice.domain.trip_search.filter.Filter
import java.time.LocalDateTime
import java.time.ZoneOffset

/**
 * Interface defining a compliance policy for a trip.
 *
 * This interface is used to ensure that a trip adheres to specific criteria or rules established.
 * Any class implementing this interface must provide an implementation for the [isCompliant] method,
 * which determines whether a specific trip complies the imposed policy.
 *
 */
interface Policy : Constraint {

    /**
     * Evaluates whether the provided trip complies with the policy.
     *
     * This method must be implemented by any class that wishes to define
     * a new compliance policy. It should return `true` if the trip adheres to the policy,
     * or `false` otherwise.
     *
     * @param trip The trip to be evaluated against this policy.
     * @return `true` if the trip complies with the policy, `false` otherwise.
     */
    fun isCompliant(trip: Trip): Boolean
}

sealed class Rule : Policy {
    override fun isCompliant(trip: Trip): Boolean {
        return trip.policies.contains(this)
    }

    object PetAllowed : Rule()
    object NoSmoking : Rule()

    companion object {
        fun valueOf(filter: Filter): Rule? {
            return when (filter) {
                Filter.PET_ALLOWED -> PetAllowed
                Filter.NO_SMOKING -> NoSmoking
                else -> null
            }
        }
    }
}

sealed class Preferences : Policy {
    object UpcomingYear : Preferences() {
        override fun isCompliant(trip: Trip): Boolean {
            val now = LocalDateTime.now()
            val oneYearLater = now.plusYears(1)
            return trip.isDepartureWithin(
                now.toInstant(ZoneOffset.UTC),
                oneYearLater.toInstant(ZoneOffset.UTC)
            )
        }
    }

    companion object {
        fun valueOf(filter: Filter): Preferences? {
            return when (filter) {
                Filter.UPCOMING_YEAR -> UpcomingYear
                else -> null
            }
        }
    }
}