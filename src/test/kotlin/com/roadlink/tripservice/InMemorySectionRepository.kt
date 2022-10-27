package com.roadlink.tripservice

import com.roadlink.tripservice.domain.Location
import com.roadlink.tripservice.domain.Section
import com.roadlink.tripservice.domain.SectionRepository
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
}