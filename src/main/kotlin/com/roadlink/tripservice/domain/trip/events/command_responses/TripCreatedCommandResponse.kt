package com.roadlink.tripservice.domain.trip.events.command_responses

import com.roadlink.tripservice.domain.trip.Trip

data class TripCreatedCommandResponse(val trip: Trip) : CommandResponse