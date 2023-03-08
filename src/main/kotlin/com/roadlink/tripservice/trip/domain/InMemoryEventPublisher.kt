package com.roadlink.tripservice.trip.domain

interface Observer {
    fun update(event: Event)
}

class CreateTripObserver : Observer {
    override fun update(event: Event) {
        // crear secciones de viaje a partir del evento
        TODO()
    }
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