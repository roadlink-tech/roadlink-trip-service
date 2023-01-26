package com.roadlink.tripservice.trip.infrastructure.persistence

import com.roadlink.tripservice.trip.domain.Location
import com.roadlink.tripservice.trip.domain.Section
import com.roadlink.tripservice.trip.domain.SectionRepository
import java.time.Instant

class InMemorySectionRepository(
    private val sections: MutableList<Section> = mutableListOf(),
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