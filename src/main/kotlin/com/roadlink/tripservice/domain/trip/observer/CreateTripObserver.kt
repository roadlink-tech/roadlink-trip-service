package com.roadlink.tripservice.domain.trip.observer

import com.roadlink.tripservice.domain.event.*
import com.roadlink.tripservice.domain.trip.section.SectionRepository

class CreateTripObserver(private val sectionRepository: SectionRepository) : Observer {
    override fun update(event: Event) {
        val tripCreatedEvent = event as TripCreatedEvent
        val trip = tripCreatedEvent.trip
        val sections = trip.sections()
        sectionRepository.save(sections)
    }
}

class CreateTripHandler(private val sectionRepository: SectionRepository) :
    CommandHandler<TripCreatedCommandV2, TripCreatedCommandResponseV2> {

    override fun handle(command: TripCreatedCommandV2): TripCreatedCommandResponseV2 {
        val trip = command.trip
        val sections = trip.sections()
        sectionRepository.save(sections)
        return TripCreatedCommandResponseV2(trip)
    }
}