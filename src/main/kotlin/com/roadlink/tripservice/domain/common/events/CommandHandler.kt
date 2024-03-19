package com.roadlink.tripservice.domain.common.events

interface CommandHandler<C : Command, R : CommandResponse> {
    fun handle(command: C): R
}