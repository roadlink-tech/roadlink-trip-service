package com.roadlink.tripservice.trip.domain

interface EventPublisher {
    fun publish(event: Event)
}