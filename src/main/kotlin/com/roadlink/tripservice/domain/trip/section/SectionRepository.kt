package com.roadlink.tripservice.domain.trip.section

import com.roadlink.tripservice.domain.Location
import com.roadlink.tripservice.domain.trip.TripPlan
import java.time.Instant
import java.util.*

interface SectionRepository {
    fun save(section: Section)
    fun saveAll(sections: Set<Section>)
    fun findNextSections(from: Location, at: Instant): Set<Section>
    fun findAllById(sectionsIds: List<String>): List<Section>
    // TODO check if should we return a list of sections?, or move it behave to a new TripPlanRepository
    fun findByTripId(tripId: UUID): TripPlan
    fun findAllByTripIds(tripIds: List<UUID>): Set<Section>
}
