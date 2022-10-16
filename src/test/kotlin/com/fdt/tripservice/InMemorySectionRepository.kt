package com.fdt.tripservice

import com.fdt.tripservice.domain.Location
import com.fdt.tripservice.domain.Section
import com.fdt.tripservice.domain.SectionRepository

class InMemorySectionRepository(
    private val sections: MutableList<Section> = mutableListOf(),
) : SectionRepository {
    override fun save(section: Section) {
        sections.add(section)
    }

    override fun findNextSectionsFrom(location: Location): Set<Section> {
        return sections.filter { it.departuresFrom(location) }.toSet()
    }
}