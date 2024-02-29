package com.roadlink.tripservice.infrastructure.rest.mappers

import com.roadlink.tripservice.domain.trip.Trip
import com.roadlink.tripservice.infrastructure.rest.responses.TripResponse

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