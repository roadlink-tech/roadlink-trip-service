package com.fdt.tripservice.domain.entity

import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TripTest {

    @Test
    fun `if a trip contain two existing locations ordered, return true for hasSubtripSection`() {
        val trip = givenAnyTrip()

        val locLikeB = Location(1L,1L)
        val locLikeD = Location(3L,3L)

        assertTrue(trip.hasSubtripSection(locLikeB,locLikeD))
    }

    @Test
    fun `if a trip doesn't contain two locations ordered, return false for hasSubtripSection`() {
        val trip = givenAnyTrip()

        val locLikeB = Location(1L,1L)
        val locLikeD = Location(3L,3L)

        assertFalse(trip.hasSubtripSection(locLikeD,locLikeB))
    }

    @Test
    fun `if a trip doesn't contain one final existing location, return false for hasSubtripSection`() {
        val trip = givenAnyTrip()

        val locLikeB = Location(1L,1L)
        val locLikeE = Location(4L,3L)

        assertFalse(trip.hasSubtripSection(locLikeB,locLikeE))
    }

    @Test
    fun `if a trip doesn't contain one initial existing location, return false for hasSubtripSection`() {
        val trip = givenAnyTrip()

        val locLikeB = Location(1L,1L)
        val locLikeE = Location(4L,3L)

        assertFalse(trip.hasSubtripSection(locLikeE,locLikeB))
    }

    @Test
    fun `if a trip doesn't contain any locations, return false for hasSubtripSection`() {
        val trip = givenAnyTrip()

        val locLikeT = Location(2L,1L)
        val locLikeE = Location(4L,3L)

        assertFalse(trip.hasSubtripSection(locLikeT,locLikeE))
    }

    @Test
    fun `if a trip hasAvailableSeatAt section return true`() {
        val trip = givenAnyTrip()

        val locB = Location(1L, 1L)
        val locC = Location(2L, 2L)
        val subtripSection = SubtripSection(locB,locC)

        assertTrue(trip.hasAvailableSeatAt(subtripSection))
    }

    @Test
    fun `if a trip doesn't hasAvailableSeatAt section return false`() {
        val trip = givenAnyTrip(0)

        val locB = Location(1L, 1L)
        val locC = Location(2L, 2L)
        val subtripSection = SubtripSection(locB,locC)

        assertFalse(trip.hasAvailableSeatAt(subtripSection))
    }

    @Test
    fun `join to unique seat trip and left no seats open`() {
        val trip = givenAnyTrip(1)

        val locB = Location(1L, 1L)
        val locC = Location(2L, 2L)
        trip.joinPassengerAt(1L, SubtripSection(locB,locC))

        val locA = Location(0L, 0L)
        val locD = Location(3L, 3L)

        assertFalse(trip.hasAvailableSeatAt(SubtripSection(locA,locD)))
    }

    private fun givenAnyTrip(capacity: Int = 4): Trip {
        val locA = Location(0L, 0L)
        val locB = Location(1L, 1L)
        val locC = Location(2L, 2L)
        val locD = Location(3L, 3L)
        return Trip(listOf(locA, locB, locC, locD), capacity)
    }
}