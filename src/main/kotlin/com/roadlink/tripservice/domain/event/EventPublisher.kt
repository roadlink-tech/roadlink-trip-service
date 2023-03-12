package com.roadlink.tripservice.domain.event

interface EventPublisher {
    fun suscribe(observer: Observer)
    fun publish(event: Event)
}