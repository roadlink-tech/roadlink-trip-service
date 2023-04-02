package com.roadlink.tripservice.usecases

import com.roadlink.tripservice.domain.AlreadyExistsTripByDriverInTimeRange
import com.roadlink.tripservice.domain.IdGenerator
import com.roadlink.tripservice.domain.time.TimeProvider
import com.roadlink.tripservice.domain.time.TimeRange
import com.roadlink.tripservice.domain.time.exception.InvalidTripTimeRangeException
import com.roadlink.tripservice.domain.trip.Trip
import com.roadlink.tripservice.domain.trip.TripPoint
import com.roadlink.tripservice.domain.trip.TripRepository
import com.roadlink.tripservice.domain.trip.events.CommandBus
import com.roadlink.tripservice.domain.trip.events.command_responses.TripCreatedCommandResponse
import com.roadlink.tripservice.domain.trip.events.commands.TripCreatedCommand

class CreateTrip(
    private val tripRepository: TripRepository,
    private val idGenerator: IdGenerator,
    private val commandBus: CommandBus,
    private val timeProvider: TimeProvider,
) {
    operator fun invoke(input: Input): Trip {
        validateTripTimeRange(input)
        validateExistsTripByDriverAndInTimeRange(input)

        val trip = input.toTrip()
        return trip.also {
            save(it)
            publishTripCreatedEvent(it)
        }
    }

    private fun validateTripTimeRange(input: Input) {
        val tripPoints = listOf(input.departure) + input.meetingPoints + listOf(input.arrival)
        for ((actual, next) in tripPoints.windowed(2, 1) { Pair(it[0], it[1]) }) {
            if (next.estimatedArrivalTime.isBefore(actual.estimatedArrivalTime))
                throw InvalidTripTimeRangeException()
        }
    }

    private fun validateExistsTripByDriverAndInTimeRange(input: Input) {
        val driver = input.driver
        val timeRange = TimeRange(from = input.departure.estimatedArrivalTime, to = input.arrival.estimatedArrivalTime)
        if (tripRepository.existsByDriverAndInTimeRange(driver, timeRange))
            throw AlreadyExistsTripByDriverInTimeRange(driver, timeRange)
    }

    private fun Input.toTrip(): Trip =
        Trip(
            id = idGenerator.id(),
            driver = driver,
            vehicle = vehicle,
            departure = departure,
            arrival = arrival,
            meetingPoints = meetingPoints,
            availableSeats = availableSeats,
        )

    private fun save(trip: Trip) {
        tripRepository.save(trip)
    }

    private fun publishTripCreatedEvent(trip: Trip) {
        commandBus.publish<TripCreatedCommand, TripCreatedCommandResponse>(
            TripCreatedCommand(
                trip = trip,
                at = timeProvider.now()
            )
        )
    }

    data class Input(
        val driver: String,
        val vehicle: String,
        val departure: TripPoint,
        val arrival: TripPoint,
        val meetingPoints: List<TripPoint>,
        val availableSeats: Int,
    )
}