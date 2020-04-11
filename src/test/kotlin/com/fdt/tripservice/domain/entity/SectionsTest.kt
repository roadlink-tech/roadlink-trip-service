package com.fdt.tripservice.domain.entity

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SectionsTest {

    @Test
    fun `given subtrip is a section`() {
        val sections = givenAnySections()

        val locLikeB = Location(1L, 1L)
        val locLikeC = Location(2L, 2L)

        assertEquals(listOf<Section>(Section(locLikeB,locLikeC,0)),
                sections.getSectionsFrom(SubtripSection(locLikeB,locLikeC)))
    }

    @Test
    fun `given subtrip is a final limit`() {
        val sections = givenAnySections()

        val locLikeB = Location(1L, 1L)
        val locLikeC = Location(2L, 2L)
        val locLikeD = Location(3L, 3L)

        assertEquals(listOf<Section>(Section(locLikeB,locLikeC,0),Section(locLikeC,locLikeD,0)),
                sections.getSectionsFrom(SubtripSection(locLikeB,locLikeD)))
    }

    private fun givenAnySections(): Sections {
        val locA = Location(0L, 0L)
        val locB = Location(1L, 1L)
        val locC = Location(2L, 2L)
        val locD = Location(3L, 3L)
        return Sections(listOf(locA, locB, locC, locD))
    }

}