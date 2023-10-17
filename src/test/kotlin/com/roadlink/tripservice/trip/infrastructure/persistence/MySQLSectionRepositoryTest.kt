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
    fun `given no sections when find all by id then should return empty result`() {
        val section = SectionFactory.avCabildo()

        val result = repository.findAllById(setOf(section.id))

        assertEquals(emptyList<Section>(), result)
    }

    @Test
    fun `given no sections when find by trip id then should return empty result`() {
        val section = SectionFactory.avCabildo()

        val result = repository.findByTripId(section.tripId)

        assertEquals(TripPlan(listOf()), result)
    }

    @Test
    fun `given several trips when find by existent trip id then should return only one section that belong to the given trip id`() {
        val sectionTrip1 = SectionFactory.avCabildo()

        val sectionTrip2 = SectionFactory.avCabildo4853_virreyDelPino1800()

        repository.saveAll(setOf(sectionTrip1, sectionTrip2))

        val result = repository.findByTripId(sectionTrip1.tripId)

        assertEquals(TripPlan(listOf(sectionTrip1)), result)
    }

    @Test
    fun `given several trips when find by existent trip id then should return the two sections that belong to the given trip id`() {
        val sectionTrip1 = SectionFactory.avCabildo()
        val sectionTrip1b = SectionFactory.virreyDelPino()
        val sectionTrip2 = SectionFactory.avCabildo4853_virreyDelPino1800()

        repository.saveAll(setOf(sectionTrip1, sectionTrip1b, sectionTrip2))

        val result = repository.findByTripId(sectionTrip1.tripId)

        assertEquals(TripPlan(listOf(sectionTrip1, sectionTrip1b)), result)
    }

    @Test
    fun `given several trips when find all by trip ids then should return all the sections that belong to both trip ids`() {
        val sectionTrip1 = SectionFactory.avCabildo()
        val sectionTrip1b = SectionFactory.virreyDelPino()
        val sectionTrip2 = SectionFactory.avCabildo4853_virreyDelPino1800()
        val sectionTrip2b = SectionFactory.virreyDelPino1800_avCabildo20()
        val sectionTrip3 = SectionFactory.virreyDelPino1800_avDelLibertador5000(
            tripId = UUID.fromString("bd7ee293-f5d3-4832-a74c-17e9a8fa4111")
        )

        repository.saveAll(setOf(sectionTrip1, sectionTrip1b, sectionTrip2, sectionTrip2b, sectionTrip3))

        val result = repository.findAllByTripIds(setOf(sectionTrip1.tripId, sectionTrip2b.tripId))

        assertEquals(setOf(sectionTrip1, sectionTrip1b, sectionTrip2, sectionTrip2b), result)
    }

    @Test
    fun `given no section when save one then should be able to find it`() {
        val section = SectionFactory.avCabildo()
        repository.save(section)

        val result = repository.findAllById(setOf(section.id))

        assertEquals(listOf(section), result)
    }

    @Test
    fun `given no section when save several sections then should be able to find the given one`() {
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
    fun `given no section when save several sections then should be able to find all the given ones`() {
        val section = SectionFactory.avCabildo()
        val section2 = SectionFactory.virreyDelPino2880_avCabildo1621()
        val section3 = SectionFactory.virreyDelPino()

        val setSection = setOf(section, section2, section3)

        repository.saveAll(setSection)

        val result = repository.findAllById(setOf(section.id, section2.id, section3.id))

        assertTrue(result.containsAll(listOf(section, section2, section3)))
    }

    @Test
    fun `when find next sections then should sections that departure from given location an after the given instant`() {
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
    fun `given no sections when find next sections then should return empty result`() {
        val result = repository.findNextSections(
            from = LocationFactory.avCabildo_4853(),
            at = InstantFactory.october15_12hs(),
        )

        assertTrue(result.isEmpty())
    }

    @Test
    fun `given no section departure from given location when find next section then should return empty result`() {
        val section = SectionFactory.virreyDelPino1800_avCabildo20()
        repository.saveAll(setOf(section))

        val result = repository.findNextSections(
            from = LocationFactory.avCabildo_4853(),
            at = InstantFactory.october15_12hs(),
        )

        assertTrue(result.isEmpty())
    }

    @Test
    fun `given no section departure after the given instant when find next sections then should return empty result`() {
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
