package com.roadlink.tripservice.usecases.trip

import com.roadlink.tripservice.domain.trip.exception.AlreadyExistsTripByDriverInTimeRange
import com.roadlink.tripservice.domain.common.IdGenerator
import com.roadlink.tripservice.domain.common.utils.time.TimeProvider
import com.roadlink.tripservice.domain.common.utils.time.TimeRange
import com.roadlink.tripservice.domain.common.utils.time.exception.InvalidTripTimeRange
import com.roadlink.tripservice.domain.trip.Trip
import com.roadlink.tripservice.domain.common.TripPoint
import com.roadlink.tripservice.domain.trip.TripRepository
import com.roadlink.tripservice.domain.common.events.CommandBus
import com.roadlink.tripservice.domain.trip.constraint.Policy
import com.roadlink.tripservice.domain.trip.constraint.Restriction
import com.roadlink.tripservice.domain.trip.events.TripCreatedEventResponse
import com.roadlink.tripservice.domain.trip.events.TripCreatedEvent

class CreateTrip(
    private val tripRepository: TripRepository,
    private val idGenerator: IdGenerator,
    private val commandBus: CommandBus,
    private val timeProvider: TimeProvider,
) {
    operator fun invoke(input: Input): Trip {
        validateTripTimeRange(input)
        validateExistsTripByDriverAndInTimeRange(input)

        return input.toTrip().also { trip ->
            trip.save(tripRepository)
            publishTripCreatedEvent(trip)
        }
    }

    private fun validateTripTimeRange(input: Input) {
        val tripPoints = listOf(input.departure) + input.meetingPoints + listOf(input.arrival)
        for ((actual, next) in tripPoints.windowed(2, 1) { Pair(it[0], it[1]) }) {
            if (next.estimatedArrivalTime.isBefore(actual.estimatedArrivalTime))
                throw InvalidTripTimeRange(
                    actualTripPointEstimatedArrivalTime = actual.estimatedArrivalTime,
                    nextTripPointEstimatedArrivalTime = next.estimatedArrivalTime,
                )
        }
    }

    private fun validateExistsTripByDriverAndInTimeRange(input: Input) {
        val driver = input.driver
        val timeRange = TimeRange(
            from = input.departure.estimatedArrivalTime,
            to = input.arrival.estimatedArrivalTime
        )
        if (tripRepository.existsByDriverAndInTimeRange(driver, timeRange)) {
            throw AlreadyExistsTripByDriverInTimeRange(driver, timeRange)
        }
    }

    private fun Input.toTrip(): Trip =
        Trip(
            id = idGenerator.id(),
            driverId = driver,
            vehicle = vehicle,
            departure = departure,
            arrival = arrival,
            meetingPoints = meetingPoints,
            availableSeats = availableSeats,
            policies = policies,
            restrictions = restrictions
        )

    private fun publishTripCreatedEvent(trip: Trip) {
        commandBus.publish<TripCreatedEvent, TripCreatedEventResponse>(
            TripCreatedEvent(
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
        val policies: List<Policy>,
        val restrictions: List<Restriction>
    )
}