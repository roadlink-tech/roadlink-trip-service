package com.fdt.tripservice.domain.entity

import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TripTest {

    @Test
    fun `if a trip contain two locations ordered, return true for hasSection`() {
        val locA = Location(0L,0L)
        val locB = Location(1L,1L)
        val locC = Location(2L,2L)
        val locD = Location(3L,3L)
        val trip = Trip(listOf(locA,locB,locC,locD))

        val locLikeB = Location(1L,1L)
        val locLikeD = Location(3L,3L)

        assertTrue(trip.hasSection(locLikeB,locLikeD))
    }

    @Test
    fun `if a trip dont contain two locations ordered, return true for hasSection`() {
        val locA = Location(0L,0L)
        val locB = Location(1L,1L)
        val locC = Location(2L,2L)
        val locD = Location(3L,3L)
        val trip = Trip(listOf(locA,locB,locC,locD))

        val locLikeB = Location(1L,1L)
        val locLikeD = Location(3L,3L)

        assertFalse(trip.hasSection(locLikeD,locLikeB))
    }

}