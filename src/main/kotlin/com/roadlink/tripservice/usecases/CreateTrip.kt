package com.roadlink.tripservice.usecases

import com.roadlink.tripservice.domain.*
import com.roadlink.tripservice.domain.event.EventPublisher
import com.roadlink.tripservice.domain.event.TripCreatedEvent
import com.roadlink.tripservice.domain.time.exception.InvalidTripTimeRangeException
import com.roadlink.tripservice.domain.time.TimeProvider
import com.roadlink.tripservice.domain.time.TimeRange
import com.roadlink.tripservice.domain.trip.Trip
import com.roadlink.tripservice.domain.trip.TripPoint
import com.roadlink.tripservice.domain.trip.TripRepository

class DefaultIdGenerator : IdGenerator {
    override fun id(): String {
        TODO("Not yet implemented")
    }

}
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
                throw InvalidTripTimeRangeException()
        }
    }

    private fun validateExistsTripByDriverAndInTimeRange(request: Request) {
        val driver = request.driver
        val timeRange = TimeRange(from = request.departure.at, to = request.arrival.at)
        if (tripRepository.existsByDriverAndInTimeRange(driver, timeRange))
            throw com.roadlink.tripservice.domain.AlreadyExistsTripByDriverInTimeRange(driver, timeRange)
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