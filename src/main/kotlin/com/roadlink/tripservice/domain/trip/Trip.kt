package com.roadlink.tripservice.domain.trip

import com.roadlink.tripservice.domain.common.IdGenerator
import com.roadlink.tripservice.domain.common.TripPoint
import com.roadlink.tripservice.domain.trip.constraint.Policy
import com.roadlink.tripservice.domain.trip.constraint.Preferences
import com.roadlink.tripservice.domain.trip.constraint.Restriction
import com.roadlink.tripservice.domain.trip.constraint.Rule
import com.roadlink.tripservice.domain.trip.constraint.Visibility
import com.roadlink.tripservice.domain.trip.section.Section
import com.roadlink.tripservice.domain.trip_search.DistanceOnEarthInMeters
import com.roadlink.tripservice.domain.trip_search.filter.Filter
import com.roadlink.tripservice.domain.user.User
import java.time.Instant
import java.util.*


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
    val policies: List<Policy> = emptyList(),
    val restrictions: List<Restriction> = emptyList()
) {

    fun canAdmitPassenger(passenger: User): Boolean {
        return this.restrictions.all {
            it.isAllowed(
                passenger,
                this
            )
        } && passenger.id != this.driverId
    }

    fun isDepartureWithin(start: Instant, end: Instant): Boolean {
        return departure.estimatedArrivalTime.isAfter(start) && departure.estimatedArrivalTime.isBefore(
            end
        )
    }

    fun isCompliant(requesterPassenger: User, filters: Set<Filter>): Boolean {
        // the restrictions always must be evaluated
        if (!canAdmitPassenger(requesterPassenger)) {
            return false
        }
        if (filters.isNotEmpty()) {
            val anyBrokenRule = filters
                .mapNotNull { Rule.valueOf(it) }
                .any { rule -> !rule.isCompliant(this) }

            val containsRestriction = filters
                .mapNotNull { Visibility.valueOf(it) }
                .all { restriction -> this.restrictions.contains(restriction) }

            val anyBrokenPreference = filters
                .mapNotNull { Preferences.valueOf(it) }
                .any { preference -> !preference.isCompliant(this) }

            return !anyBrokenRule && containsRestriction && !anyBrokenPreference
        }
        return true
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

    enum class Status {
        NOT_STARTED,
        IN_PROGRESS,
        FINISHED
    }
}
