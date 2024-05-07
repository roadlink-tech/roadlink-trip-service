package com.roadlink.tripservice.domain.trip

import com.roadlink.tripservice.domain.common.IdGenerator
import com.roadlink.tripservice.domain.common.TripPoint
import com.roadlink.tripservice.domain.common.utils.time.TimeRange
import com.roadlink.tripservice.domain.trip.section.Section
import com.roadlink.tripservice.domain.trip_search.DistanceOnEarthInMeters
import com.roadlink.tripservice.domain.user.User
import java.util.*

enum class Filter {
    PET_ALLOWED,
    NO_SMOKING,
    ONLY_WOMEN,
    PRIVATE,
    FRIENDS_OF_FRIENDS
}


interface Restriction {
    /**
     * isAllowed returns true if the trip can be selected base on the restriction
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

sealed class Rule {
    object PetAllowed : Rule()
    object NoSmoking : Rule()
    object OnlyWomen : Rule()

    companion object {
        fun valueOf(filter: Filter): Rule? {
            return when (filter) {
                Filter.PET_ALLOWED -> PetAllowed
                Filter.NO_SMOKING -> NoSmoking
                Filter.ONLY_WOMEN -> OnlyWomen
                else -> null
            }
        }
    }
}

data class Trip(
    val id: String,
    // TODO Is it the driver id? if it's then why not use a UUID instead of string
    val driverId: String,
    val vehicle: String,
    val departure: TripPoint,
    val arrival: TripPoint,
    val status: Status = Status.NOT_STARTED,
    val meetingPoints: List<TripPoint>,
    // TODO rename it by seats to be used. It'll be the vehicle capacity
    val availableSeats: Int,
    val rules: List<Rule> = emptyList(),
    val restrictions: List<Restriction> = emptyList()
) {

    fun satisfy(requesterPassenger: User, filters: List<Filter>): Boolean {
        if (filters.isNotEmpty()) {
            val rules = filters.mapNotNull { Rule.valueOf(it) }
            if (rules.isNotEmpty() && !this.rules.containsAll(rules)) {
                return false
            }
            val visibilityRestrictions = filters.mapNotNull { Visibility.valueOf(it) }
            return visibilityRestrictions.none { !it.isAllowed(requesterPassenger, this) }
        }
        return false
    }

    // TODO revisar la creacion del trip y como lo itero
    fun sections(idGenerator: IdGenerator): Set<Section> {
        val allTripPoints = buildList {
            add(departure)
            addAll(meetingPoints)
            add(arrival)
        }
        val sections = mutableListOf<Section>()
        for (i in allTripPoints.indices) {
            if (isTheEndOfTheWay(allTripPoints, i)) {
                return sections.toSet()
            }
            val sectionDeparture = allTripPoints[i]
            val sectionArrival = allTripPoints[i + 1]
            val section = Section(
                id = idGenerator.id(),
                tripId = UUID.fromString(this.id),
                departure = sectionDeparture,
                arrival = sectionArrival,
                distanceInMeters = DistanceOnEarthInMeters(
                    sectionDeparture.location(),
                    sectionArrival.location()
                ),
                driverId = driverId,
                vehicleId = vehicle,

                initialAmountOfSeats = availableSeats,
                bookedSeats = 0
            )
            sections.add(section)
        }
        return sections.toSet()
    }

    private fun isTheEndOfTheWay(
        allTripPoints: List<TripPoint>,
        i: Int
    ) = allTripPoints[i] == arrival

    fun isInTimeRange(timeRange: TimeRange): Boolean =
        TimeRange(departure.estimatedArrivalTime, arrival.estimatedArrivalTime).intersects(timeRange)

    enum class Status {
        NOT_STARTED,
        IN_PROGRESS,
        FINISHED
    }
}
