package com.roadlink.tripservice.trip.infrastructure.persistence

import com.roadlink.tripservice.domain.trip.TripPlan
import com.roadlink.tripservice.domain.trip.section.Section
import com.roadlink.tripservice.infrastructure.persistence.MySQLSectionRepository
import com.roadlink.tripservice.trip.domain.InstantFactory
import com.roadlink.tripservice.trip.domain.LocationFactory
import com.roadlink.tripservice.trip.domain.SectionFactory
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.*


@MicronautTest
class MySQLSectionRepositoryTest {

    @Inject
    lateinit var repository: MySQLSectionRepository

    @Test
    fun `test empty database should return empty result`() {
        val section = SectionFactory.avCabildo()

        val result = repository.findAllById(setOf(section.id))

        assertEquals(emptyList<Section>(), result)
    }

    @Test
    fun `test empty database should return empty result finding by tripId`() {
        val section = SectionFactory.avCabildo()

        val result = repository.findByTripId(section.tripId)

        assertEquals(TripPlan(listOf()), result)
    }

    @Test
    fun `test find only one section that belong to tripId where several trips are in the database`() {
        val sectionTrip1 = SectionFactory.avCabildo()

        val sectionTrip2 = SectionFactory.avCabildo4853_virreyDelPino1800()

        repository.saveAll(setOf(sectionTrip1, sectionTrip2))

        val result = repository.findByTripId(sectionTrip1.tripId)

        assertEquals(TripPlan(listOf(sectionTrip1)), result)
    }

    @Test
    fun `test find two sections that belong to tripId where several trips are in the database`() {
        val sectionTrip1 = SectionFactory.avCabildo()
        val sectionTrip1b = SectionFactory.virreyDelPino()
        val sectionTrip2 = SectionFactory.avCabildo4853_virreyDelPino1800()

        repository.saveAll(setOf(sectionTrip1, sectionTrip1b, sectionTrip2))

        val result = repository.findByTripId(sectionTrip1.tripId)

        assertEquals(TripPlan(listOf(sectionTrip1, sectionTrip1b)), result)
    }

    @Test
    fun `test find two sections that belong to different tripId where several trips are in the database`() {
        val sectionTrip1 = SectionFactory.avCabildo()
        val sectionTrip1b = SectionFactory.virreyDelPino()
        val sectionTrip2 = SectionFactory.avCabildo4853_virreyDelPino1800()
        val sectionTrip2b = SectionFactory.virreyDelPino1800_avCabildo20()
        val sectionTrip3 = SectionFactory.virreyDelPino1800_avDelLibertador5000(tripId =
        UUID.fromString("bd7ee293-f5d3-4832-a74c-17e9a8fa4111"))

        repository.saveAll(setOf(sectionTrip1, sectionTrip1b, sectionTrip2, sectionTrip2b, sectionTrip3))

        val result = repository.findAllByTripIds(setOf(sectionTrip1.tripId, sectionTrip2b.tripId))

        assertEquals(setOf(sectionTrip1, sectionTrip1b, sectionTrip2, sectionTrip2b), result)
    }


    @Test
    fun `test save and retrieve`() {
        val section = SectionFactory.avCabildo()
        repository.save(section)

        val result = repository.findAllById(setOf(section.id))

        assertEquals(listOf(section), result)
    }

    @Test
    fun `test save various sections and retrieve only the one i want`() {
        val section = SectionFactory.avCabildo()
        repository.save(section)

        val section2 = SectionFactory.virreyDelPino2880_avCabildo1621()
        repository.save(section2)

        val section3 = SectionFactory.virreyDelPino()
        repository.save(section3)

        val result = repository.findAllById(setOf(section.id))

        assertEquals(listOf(section), result)
    }

    @Test
    fun `test save a set of sections`() {
        val section = SectionFactory.avCabildo()
        val section2 = SectionFactory.virreyDelPino2880_avCabildo1621()
        val section3 = SectionFactory.virreyDelPino()

        val setSection = setOf(section, section2, section3)

        repository.saveAll(setSection)

        val result = repository.findAllById(setOf(section.id, section2.id, section3.id))

        assertTrue(result.containsAll(listOf(section, section2, section3)))
    }

    @Test
    fun `should find next sections given they departure from given location an after the given instant`() {
        val section1 = SectionFactory.avCabildo()
        val section2 = SectionFactory.avCabildo4853_virreyDelPino1800()
        repository.saveAll(setOf(section1, section2))

        val result = repository.findNextSections(
            from = LocationFactory.avCabildo_4853(),
            at = InstantFactory.october15_12hs(),
        )

        assertTrue(result.containsAll(listOf(section1, section2)))
    }

    @Test
    fun `should return empty set when find next section given no section exists`() {
        val result = repository.findNextSections(
            from = LocationFactory.avCabildo_4853(),
            at = InstantFactory.october15_12hs(),
        )

        assertTrue(result.isEmpty())
    }

    @Test
    fun `should return empty set when find next section given no section departure from given location`() {
        val section = SectionFactory.virreyDelPino1800_avCabildo20()
        repository.saveAll(setOf(section))

        val result = repository.findNextSections(
            from = LocationFactory.avCabildo_4853(),
            at = InstantFactory.october15_12hs(),
        )

        assertTrue(result.isEmpty())
    }

    @Test
    fun `should return empty set when find next section given no section departure after the given instant`() {
        val section1 = SectionFactory.avCabildo()
        val section2 = SectionFactory.avCabildo4853_virreyDelPino1800()
        repository.saveAll(setOf(section1, section2))

        val result = repository.findNextSections(
            from = LocationFactory.avCabildo_4853(),
            at = InstantFactory.october15_22hs(),
        )

        assertTrue(result.isEmpty())
    }

}
