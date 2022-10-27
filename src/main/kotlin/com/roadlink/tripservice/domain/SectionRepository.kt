package com.roadlink.tripservice.domain

import java.time.Instant

interface SectionRepository {
    fun save(section: Section)
    fun findNextSections(from: Location, at: Instant): Set<Section>
}
