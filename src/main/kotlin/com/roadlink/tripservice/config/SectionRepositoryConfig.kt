package com.roadlink.tripservice.config

import com.roadlink.tripservice.domain.Location
import com.roadlink.tripservice.domain.trip.Address
import com.roadlink.tripservice.domain.trip.TripPoint
import com.roadlink.tripservice.domain.trip.section.Section
import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.infrastructure.persistence.InMemorySectionRepository
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton
import java.time.Instant

@Factory
class SectionRepositoryConfig {
    @Singleton
    fun sectionRepository(): SectionRepository {
        return InMemorySectionRepository(sections = mutableListOf(avCabildoSection()))
    }

    private fun avCabildoSection(): Section =
        Section(
            departure = TripPoint(
                estimatedArrivalTime = Instant.parse("2022-10-15T12:00:00Z"),
                address = Address(
                    location = Location(
                        latitude = -34.54025770408163,
                        longitude = -58.47450726734694,
                        alias = "AvCabildo 4853",
                    ),
                    fullAddress = "Av. Cabildo 4853, Buenos Aires",
                    street = "Av. Cabildo",
                    city = "Buenos Aires",
                    country = "Argentina",
                    housenumber = "4853",
                ),
            ),
            arrival = TripPoint(
                estimatedArrivalTime = Instant.parse("2022-10-15T13:00:00Z"),
                address = Address(
                    location = Location(
                        latitude = -34.57489533621165,
                        longitude = -58.435972139652776,
                        alias = "AvCabildo 20",
                    ),
                    fullAddress = "Av. Cabildo 20, Buenos Aires",
                    street = "Av. Cabildo",
                    city = "Buenos Aires",
                    country = "Argentina",
                    housenumber = "20",
                ),
            ),
            distanceInMeters = 6070.0,
            driver = "John Smith",
            vehicle = "Ford mustang",
            availableSeats = 4,
        )
}