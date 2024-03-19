package com.roadlink.tripservice.domain.common.events

interface CommandBus {
    fun <C : Command, R : CommandResponse> publish(command: C): R
}