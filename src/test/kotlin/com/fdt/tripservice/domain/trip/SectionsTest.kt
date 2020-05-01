package com.fdt.tripservice.domain.trip

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SectionsTest {
    @Test
    fun `given subtrip is a section`() {
        val sections = givenAnySections()

        val locLikeB = Location(1L, 1L)
        val locLikeC = Location(2L, 2L)

        assertEquals(listOf(Section(locLikeB,locLikeC, mutableListOf())),
                sections[Subtrip(locLikeB,locLikeC)])
    }

    @Test
    fun `given subtrip is a final limit`() {
        val sections = givenAnySections()

        val locLikeB = Location(1L, 1L)
        val locLikeC = Location(2L, 2L)
        val locLikeD = Location(3L, 3L)

        assertEquals(listOf(Section(locLikeB,locLikeC, mutableListOf()),Section(locLikeC,locLikeD, mutableListOf())),
                sections[Subtrip(locLikeB,locLikeD)])
    }

    private fun givenAnySections(): Sections {
        val locA = Location(0L, 0L)
        val locB = Location(1L, 1L)
        val locC = Location(2L, 2L)
        val locD = Location(3L, 3L)
        return Sections(listOf(locA, locB, locC, locD))
    }
}