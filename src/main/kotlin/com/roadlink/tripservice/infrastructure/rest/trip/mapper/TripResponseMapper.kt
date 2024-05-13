package com.roadlink.tripservice.infrastructure.rest.trip.mapper

import com.roadlink.tripservice.domain.trip.Trip
import com.roadlink.tripservice.domain.trip.constraint.Policy
import com.roadlink.tripservice.domain.trip.constraint.Restriction
import com.roadlink.tripservice.domain.trip.constraint.Rule
import com.roadlink.tripservice.domain.trip.constraint.Visibility
import com.roadlink.tripservice.infrastructure.rest.common.trip_point.TripPointResponseMapper
import com.roadlink.tripservice.infrastructure.rest.trip.response.TripResponse

object TripResponseMapper {
    fun map(trip: Trip) =
        TripResponse(
            id = trip.id,
            driver = trip.driverId,
            vehicle = trip.vehicle,
            departure = TripPointResponseMapper.map(trip.departure),
            meetingPoints = trip.meetingPoints.map { TripPointResponseMapper.map(it) },
            arrival = TripPointResponseMapper.map(trip.arrival),
            availableSeats = trip.availableSeats,
            policies = trip.policies.map { PolicyResponseMapper.map(it) },
            restrictions = trip.restrictions.map { RestrictionResponseMapper.map(it) }
        )

    class PolicyResponseMapper {
        companion object {
            fun map(policy: Policy): String {
                return when (policy) {
                    is Rule.PetAllowed -> "PET_ALLOWED"
                    is Rule.NoSmoking -> "NO_SMOKING"
                    else -> throw IllegalArgumentException("Unknown policy: $policy")
                }
            }
        }
    }

    class RestrictionResponseMapper {
        companion object {
            fun map(restriction: Restriction): String {
                return when (restriction) {
                    is Visibility.Private -> "PRIVATE"
                    is Visibility.OnlyWomen -> "ONLY_WOMEN"
                    else -> throw IllegalArgumentException("Unknown restriction: $restriction")
                }
            }
        }
    }
}