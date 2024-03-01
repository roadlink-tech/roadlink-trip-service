package com.roadlink.tripservice.infrastructure.persistence

import com.roadlink.tripservice.domain.Location
import com.roadlink.tripservice.domain.trip_search.TripPlan
import com.roadlink.tripservice.domain.trip.section.Section
import com.roadlink.tripservice.domain.trip.section.SectionRepository
import java.time.Instant
import java.util.*

class InMemorySectionRepository(
    private val sections: MutableList<Section> = mutableListOf(),
) : SectionRepository {

    override fun save(section: Section) {
        sections.add(section)
    }

    override fun saveAll(sections: Set<Section>) {
        sections.forEach { section ->
            this.save(section)
        }
    }

    override fun findNextSections(from: Location, at: Instant): Set<Section> {
        return sections
            .filter { it.departuresFrom(from) }
            .filter { it.departuresAfterOrEqual(at) }
            .toSet()
    }

    override fun findAllById(sectionsIds: Set<String>): List<Section> {
        return sectionsIds
            .mapNotNull { sectionId ->
                sections.firstOrNull { it.id == sectionId }
            }
    }

    override fun findAllByTripIds(tripIds: Set<UUID>): Set<Section> {
        return sections
            .filter { it.tripId in tripIds }
            .toSet()
    }

    override fun findByTripId(tripId: UUID): TripPlan {
        return sections
            .filter { it.tripId == tripId }
            .sortedBy { it.departure.estimatedArrivalTime }
            .let { TripPlan(it) }
    }

    fun deleteAll() {
        sections.clear()
    }
}