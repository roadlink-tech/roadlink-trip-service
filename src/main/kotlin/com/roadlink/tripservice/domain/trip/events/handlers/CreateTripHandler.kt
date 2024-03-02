package com.roadlink.tripservice.domain.trip.events.handlers

import com.roadlink.tripservice.domain.common.IdGenerator
import com.roadlink.tripservice.domain.trip.events.command_responses.TripCreatedCommandResponse
import com.roadlink.tripservice.domain.trip.events.commands.TripCreatedCommand
import com.roadlink.tripservice.domain.trip.section.SectionRepository

class CreateTripHandler(
    private val idGenerator: IdGenerator,
    private val sectionRepository: SectionRepository,
) :
    CommandHandler<TripCreatedCommand, TripCreatedCommandResponse> {

    override fun handle(command: TripCreatedCommand): TripCreatedCommandResponse {
        val trip = command.trip
        val sections = trip.sections(idGenerator)
        sectionRepository.saveAll(sections)
        return TripCreatedCommandResponse(trip)
    }
}
