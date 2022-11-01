package com.roadlink.tripservice.infrastructure.repositories

import com.roadlink.tripservice.domain.Location
import com.roadlink.tripservice.domain.Section
import com.roadlink.tripservice.domain.SectionRepository
import com.roadlink.tripservice.domain.TripPoint
import java.time.Instant

class InMemorySectionRepository(
    private val sections: MutableList<Section> = mutableListOf(
        Section(
            departure = TripPoint(
                location = Location(latitude = -34.540412, longitude = -58.474732, alias = "AvCabildo 4853"),
                at = Instant.parse("2022-10-15T12:00:00Z"),
            ),
            arrival = TripPoint(
                location = Location(latitude = -34.574810, longitude = -58.435990, alias = "AvCabildo 20"),
                at = Instant.parse("2022-10-15T13:00:00Z"),
            ),
            distanceInMeters = 6070.0,
        )
    ),
) : SectionRepository {
    override fun save(section: Section) {
        sections.add(section)
    }

    override fun findNextSections(from: Location, at: Instant): Set<Section> {
        return sections
            .filter { it.departuresFrom(from) }
            .filter { it.departuresAfterOrEqual(at) }
            .toSet()
    }

    fun deleteAll() {
        sections.clear()
    }
}