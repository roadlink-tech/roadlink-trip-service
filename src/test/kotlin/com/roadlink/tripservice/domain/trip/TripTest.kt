package com.roadlink.tripservice.domain.trip

import com.roadlink.tripservice.domain.trip.section.Section
import com.roadlink.tripservice.trip.domain.TripFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TripTest {

    @Test
    fun `when a trip has a meeting point then 2 sections must be created`() {
        // given
        val trip = givenATripWithAMeetingPoint()

        // when
        val sections = trip.sections()

        // then
        thenTheExpectedAmountOfSectionsWereCreated(2, sections)
    }

    @Test
    fun `when a trip has 2 meeting points then 3 sections must be created`() {
        // given
        val trip = givenATripWithTwoMeetingPoint()

        // when
        val sections = trip.sections()

        // then
        thenTheExpectedAmountOfSectionsWereCreated(3, sections)
    }

    private fun givenATripWithTwoMeetingPoint(): Trip {
        return TripFactory.caba_escobar_pilar_rosario()
    }

    private fun thenTheExpectedAmountOfSectionsWereCreated(expectedSections: Int, sections: Set<Section>) {
        assertEquals(expectedSections, sections.size)
    }

    private fun givenATripWithAMeetingPoint(): Trip {
        return TripFactory.avCabildo4853_virreyDelPino1800_avCabildo20()
    }
}