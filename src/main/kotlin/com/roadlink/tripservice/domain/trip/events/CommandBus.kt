package com.roadlink.tripservice.domain.trip.events

import com.roadlink.tripservice.domain.trip.events.command_responses.CommandResponse
import com.roadlink.tripservice.domain.trip.events.commands.Command

interface CommandBus {
    fun <C : Command, R : CommandResponse> publish(command: C): R
}