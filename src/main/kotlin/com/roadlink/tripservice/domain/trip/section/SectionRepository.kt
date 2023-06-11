package com.roadlink.tripservice.domain.trip.section

import com.roadlink.tripservice.domain.Location
import java.time.Instant
import java.util.*

interface SectionRepository {
    fun save(section: Section)
    fun save(sections: Set<Section>)
    fun findNextSections(from: Location, at: Instant): Set<Section>
    fun findAllById(sectionsIds: Set<String>): Set<Section>
    fun findAllByTripIds(tripIds: List<UUID>): Set<Section>
}
