package com.roadlink.tripservice.trip.usecases

import com.roadlink.tripservice.trip.domain.*

class CreateTrip(
    private val tripRepository: TripRepository,
    private val idGenerator: IdGenerator,
    private val eventPublisher: EventPublisher,
    private val timeProvider: TimeProvider,
) {
    operator fun invoke(request: Request): Trip {
        validateTripTimeRange(request)
        validateExistsTripByDriverAndInTimeRange(request)

        val trip = request.toTrip()
        return trip.also {
            tripRepository.save(it)
            eventPublisher.publish(TripCreatedEvent(trip = trip, at = timeProvider.now()))
        }
    }

    private fun validateTripTimeRange(request: Request) {
        val tripPoints = listOf(request.departure) + request.meetingPoints + listOf(request.arrival)
        for ((actual, next) in tripPoints.windowed(2, 1) { Pair(it[0], it[1]) }) {
            if (next.at.isBefore(actual.at))
                throw InvalidTripTimeRange()
        }
    }

    private fun validateExistsTripByDriverAndInTimeRange(request: Request) {
        val driver = request.driver
        val timeRange = TimeRange(from = request.departure.at, to = request.arrival.at)
        if (tripRepository.existsByDriverAndInTimeRange(driver, timeRange))
            throw AlreadyExistsTripByDriverInTimeRange(driver, timeRange)
    }

    private fun Request.toTrip(): Trip =
        Trip(
            id = idGenerator.id(),
            driver = driver,
            vehicle = vehicle,
            departure = departure,
            arrival = arrival,
            meetingPoints = meetingPoints,
            availableSeats = availableSeats,
        )

    data class Request(
        val driver: String,
        val vehicle: String,
        val departure: TripPoint,
        val arrival: TripPoint,
        val meetingPoints: List<TripPoint>,
        val availableSeats: Int,
    )
}