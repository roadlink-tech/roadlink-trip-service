package com.roadlink.tripservice.domain.trip.events.handlers

import com.roadlink.tripservice.domain.trip.events.command_responses.CommandResponse
import com.roadlink.tripservice.domain.trip.events.commands.Command

interface CommandHandler<C : Command, R : CommandResponse> {
    fun handle(command: C): R
}