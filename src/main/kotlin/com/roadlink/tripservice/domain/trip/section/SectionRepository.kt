package com.roadlink.tripservice.domain.trip.section

import com.roadlink.tripservice.domain.common.Location
import java.time.Instant
import java.util.*

interface SectionRepository {
    fun save(section: Section)
    fun saveAll(sections: Set<Section>)
    fun findNextSections(from: Location, at: Instant): Set<Section>
    fun findAllById(sectionsIds: Set<String>): List<Section>
    fun findAllByTripIdOrFail(tripId: UUID): List<Section>
    fun findAllByTripIds(tripIds: Set<UUID>): Set<Section>
}
