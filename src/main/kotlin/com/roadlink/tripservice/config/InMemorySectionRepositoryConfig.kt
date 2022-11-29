package com.roadlink.tripservice.config

import com.roadlink.tripservice.domain.Location
import com.roadlink.tripservice.domain.Section
import com.roadlink.tripservice.domain.TripPoint
import com.roadlink.tripservice.infrastructure.persistence.InMemorySectionRepository
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton
import java.time.Instant

@Factory
class InMemorySectionRepositoryConfig {
    @Singleton
    fun inMemorySectionRepository(): InMemorySectionRepository {
        return InMemorySectionRepository(sections = mutableListOf(
                Section(
                    departure = TripPoint(
                        location = Location(latitude = -34.540412, longitude = -58.474732, alias = "AvCabildo 4853"),
                        at = Instant.parse("2022-10-15T12:00:00Z"),
                        formatted = "Av. Cabildo 4853, Buenos Aires",
                        street = "Av. Cabildo",
                        city = "Buenos Aires",
                        country = "Argentina",
                        housenumber = "4853",
                    ),
                    arrival = TripPoint(
                        location = Location(latitude = -34.574810, longitude = -58.435990, alias = "AvCabildo 20"),
                        at = Instant.parse("2022-10-15T13:00:00Z"),
                        formatted = "Av. Cabildo 20, Buenos Aires",
                        street = "Av. Cabildo",
                        city = "Buenos Aires",
                        country = "Argentina",
                        housenumber = "20",
                    ),
                    distanceInMeters = 6070.0,
                    driver = "John Smith",
                    vehicle = "Ford mustang",
                    availableSeats = 4,
                )
            ))
    }
}