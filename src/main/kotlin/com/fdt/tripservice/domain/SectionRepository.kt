package com.fdt.tripservice.domain

interface SectionRepository {
    fun save(section: Section)
    fun findNextSectionsFrom(location: Location): Set<Section>
}