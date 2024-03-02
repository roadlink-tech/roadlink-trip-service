package com.roadlink.tripservice.domain.trip.section

import com.roadlink.tripservice.domain.common.Location
import com.roadlink.tripservice.domain.trip_search.TripPlan
import java.time.Instant
import java.util.*

interface SectionRepository {
    fun save(section: Section)
    fun saveAll(sections: Set<Section>)
    fun findNextSections(from: Location, at: Instant): Set<Section>
    fun findAllById(sectionsIds: Set<String>): List<Section>
    // TODO check if should we return a list of sections?, or move it behave to a new TripPlanRepository
    fun findByTripId(tripId: UUID): TripPlan
    fun findAllByTripIds(tripIds: Set<UUID>): Set<Section>
}
