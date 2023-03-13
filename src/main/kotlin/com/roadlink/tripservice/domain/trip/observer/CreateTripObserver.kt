package com.roadlink.tripservice.domain.trip.observer

import com.roadlink.tripservice.domain.event.Event
import com.roadlink.tripservice.domain.event.Observer
import com.roadlink.tripservice.domain.event.TripCreatedEvent
import com.roadlink.tripservice.domain.trip.section.SectionRepository

class CreateTripObserver(private val sectionRepository: SectionRepository) : Observer {
    override fun update(event: Event) {
        val tripCreatedEvent = event as TripCreatedEvent
        val trip = tripCreatedEvent.trip
        val sections = trip.sections()
        sectionRepository.save(sections)
    }
}