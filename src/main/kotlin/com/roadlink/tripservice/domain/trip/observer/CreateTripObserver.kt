package com.roadlink.tripservice.domain.trip.observer

import com.roadlink.tripservice.domain.event.Event
import com.roadlink.tripservice.domain.event.Observer

class CreateTripObserver : Observer {
    override fun update(event: Event) {
        // crear secciones de viaje a partir del evento
        TODO()
    }
}