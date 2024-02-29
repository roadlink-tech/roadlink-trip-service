package com.roadlink.tripservice.infrastructure.rest.trip.mapper

import com.roadlink.tripservice.domain.trip.Trip
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
        )
}