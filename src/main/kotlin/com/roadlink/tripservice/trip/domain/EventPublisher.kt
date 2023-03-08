package com.roadlink.tripservice.trip.domain

interface EventPublisher {
    fun suscribe(observer: Observer)
    fun publish(event: Event)
}