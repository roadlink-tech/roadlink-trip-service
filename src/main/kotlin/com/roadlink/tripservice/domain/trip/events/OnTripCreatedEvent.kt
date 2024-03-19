package com.roadlink.tripservice.domain.trip.events

import com.roadlink.tripservice.domain.common.IdGenerator
import com.roadlink.tripservice.domain.common.events.Command
import com.roadlink.tripservice.domain.common.events.CommandHandler
import com.roadlink.tripservice.domain.common.events.CommandResponse
import com.roadlink.tripservice.domain.trip.Trip
import com.roadlink.tripservice.domain.trip.section.SectionRepository
import java.time.Instant

class OnTripCreatedEventCreateSections(
    private val idGenerator: IdGenerator,
    private val sectionRepository: SectionRepository,
) :
    CommandHandler<TripCreatedEvent, TripCreatedEventResponse> {

    override fun handle(event: TripCreatedEvent): TripCreatedEventResponse {
        val trip = event.trip
        val sections = trip.sections(idGenerator)
        sectionRepository.saveAll(sections)
        return TripCreatedEventResponse(trip)
    }
}

data class TripCreatedEvent(val trip: Trip, val at: Instant) : Command
data class TripCreatedEventResponse(val trip: Trip) : CommandResponse