package com.roadlink.tripservice.domain.trip.section

import org.locationtech.jts.geom.Polygon
import java.time.Instant
import java.util.*

interface SectionRepository {
    fun save(section: Section)
    fun saveAll(sections: Set<Section>)
    // TODO usar un único find
    fun findNextSectionsIn(polygon: Polygon, at: Instant): Set<Section>
    fun findAllById(sectionsIds: Set<String>): List<Section>
    fun findAllByTripIdOrFail(tripId: UUID): List<Section>
    fun findAllByTripIds(tripIds: Set<UUID>): Set<Section>
}
