package com.roadlink.tripservice.domain

interface EventPublisher {
    fun publish(event: Event)
}