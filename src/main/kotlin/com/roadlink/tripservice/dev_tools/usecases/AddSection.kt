package com.roadlink.tripservice.dev_tools.usecases

import com.roadlink.tripservice.dev_tools.domain.Geoapify
import com.roadlink.tripservice.dev_tools.domain.AddressNotExists
import com.roadlink.tripservice.domain.trip.TripPoint
import com.roadlink.tripservice.domain.trip.section.Section
import com.roadlink.tripservice.domain.trip.section.SectionRepository
import java.time.Instant

class AddSection(
    private val geoapify: Geoapify,
    private val sectionRepository: SectionRepository,
) {
    operator fun invoke(request: Request) {
        val section = Section(
            departure = tripPoint(request.departure),
            arrival = tripPoint(request.arrival),
            distanceInMeters = request.distanceInMeters,
            driver = request.driver,
            vehicle = request.vehicle,
            availableSeats = request.availableSeats,
        )

        sectionRepository.save(section)
    }

    private fun tripPoint(request: Request.TripPoint): TripPoint {
        val address = geoapify.addressByName(name = request.name) ?: throw AddressNotExists(request.name)

        return TripPoint(
            estimatedArrivalTime = request.at,
            address = address,
        )
    }

    data class Request(
        val departure: TripPoint,
        val arrival: TripPoint,
        val distanceInMeters: Double,
        val driver: String,
        val vehicle: String,
        val availableSeats: Int,
    ) {
        data class TripPoint(
            val name: String,
            val at: Instant,
        )
    }
}


