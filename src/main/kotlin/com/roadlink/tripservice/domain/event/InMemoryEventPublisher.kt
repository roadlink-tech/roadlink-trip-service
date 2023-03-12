package com.roadlink.tripservice.domain.event

interface Observer {
    fun update(event: Event)
}

class InMemoryEventPublisher(private val observers: MutableList<Observer> = mutableListOf()) : EventPublisher {
    override fun suscribe(observer: Observer) {
        observers.add(observer)
    }

    override fun publish(event: Event) {
        observers.forEach {
            it.update(event)
        }
    }
}