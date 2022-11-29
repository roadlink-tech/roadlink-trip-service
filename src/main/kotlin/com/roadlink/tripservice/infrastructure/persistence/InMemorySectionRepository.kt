package com.roadlink.tripservice.infrastructure.persistence

import com.roadlink.tripservice.domain.Location
import com.roadlink.tripservice.domain.Section
import com.roadlink.tripservice.domain.SectionRepository
import com.roadlink.tripservice.domain.TripPoint
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