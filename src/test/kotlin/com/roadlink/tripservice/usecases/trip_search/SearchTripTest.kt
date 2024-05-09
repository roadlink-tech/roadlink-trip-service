package com.roadlink.tripservice.usecases.trip_search

import com.roadlink.tripservice.domain.common.Location
import com.roadlink.tripservice.domain.trip.section.Section
import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.domain.trip_search.algorithm.BruteForceSearchEngine
import com.roadlink.tripservice.domain.trip_search.JtsSearchCircleCreator
import com.roadlink.tripservice.domain.trip_search.SearchRadiusGenerator
import com.roadlink.tripservice.usecases.common.InstantFactory
import com.roadlink.tripservice.usecases.common.address.LocationFactory
import com.roadlink.tripservice.usecases.trip.SectionFactory
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant
import kotlin.properties.Delegates

internal class SearchTripTest {

    private lateinit var sectionRepository: SectionRepository

    private var circleSearchAreaRadius by Delegates.notNull<Double>()

    private lateinit var searchTrip: SearchTrip

    @BeforeEach
    fun setUp() {
        sectionRepository = mockk()

        val bruteForceSearchEngine = BruteForceSearchEngine(
            sectionRepository = sectionRepository,
            circleSearchAreaCreator = JtsSearchCircleCreator(),
        )

        searchTrip = SearchTrip(
            searchEngine = bruteForceSearchEngine,
        )
    }

    @Test
    fun `given no section exists then should return empty list`() {
        // GIVEN
        val departure = LocationFactory.avCabildo_4853()
        val arrival = LocationFactory.avCabildo_20()
        val at = InstantFactory.october15_12hs()
        circleSearchAreaRadius = circleSearchAreaRadius(departure, arrival)
        givenNextSections(from = departure, at = at, nextSections = emptySet())

        // WHEN
        val result = searchTrip(
            SearchTrip.Input(
                departure = departure,
                arrival = arrival,
                at = at,
            )
        )

        // THEN
        assertTrue(result.isEmpty())
    }

    @Test
    fun `given exists a trip plan between the given departure and arrival then should return it`() {
        // GIVEN
        val departure = LocationFactory.avCabildo_4853()
        val arrival = LocationFactory.avCabildo_20()
        val at = InstantFactory.october15_12hs()
        circleSearchAreaRadius = circleSearchAreaRadius(departure, arrival)
        givenNextSections(
            from = departure,
            at = at,
            nextSections = setOf(SectionFactory.avCabildo())
        )

        // WHEN
        val result = searchTrip(
            SearchTrip.Input(
                departure = departure,
                arrival = arrival,
                at = at,
            )
        )

        // THEN
        assertEquals(listOf(TripSearchPlanFactory.avCabildo()), result)
    }

    @Test
    fun `given exists a trip plan with one meeting point between the given departure and arrival then should return it`() {
        // GIVEN
        circleSearchAreaRadius =
            circleSearchAreaRadius(LocationFactory.avCabildo_4853(), LocationFactory.avCabildo_20())
        givenNextSections(
            from = LocationFactory.avCabildo_4853(),
            at = InstantFactory.october15_12hs(),
            nextSections = setOf(SectionFactory.avCabildo4853_virreyDelPino1800())
        )
        givenNextSections(
            from = LocationFactory.virreyDelPino_1800(),
            at = InstantFactory.october15_17hs(),
            nextSections = setOf(SectionFactory.virreyDelPino1800_avCabildo20())
        )

        // WHEN
        val result = searchTrip(
            SearchTrip.Input(
                departure = LocationFactory.avCabildo_4853(),
                arrival = LocationFactory.avCabildo_20(),
                at = InstantFactory.october15_12hs(),
            )
        )

        // THEN
        assertEquals(
            listOf(TripSearchPlanFactory.avCabildo4853_virreyDelPino1800_avCabildo20()),
            result
        )
    }

    @Test
    fun `given exists two trip plans with one meeting point between the given departure and arrival then the shorter must be first`() {
        // GIVEN
        circleSearchAreaRadius =
            circleSearchAreaRadius(LocationFactory.avCabildo_4853(), LocationFactory.avCabildo_20())
        givenNextSections(
            from = LocationFactory.avCabildo_4853(),
            at = InstantFactory.october15_12hs(),
            nextSections = setOf(
                SectionFactory.avCabildo4853_virreyDelPino1800(),
                SectionFactory.avCabildo4853_virreyDelPino2880()
            )
        )
        givenNextSections(
            from = LocationFactory.virreyDelPino_1800(),
            at = InstantFactory.october15_17hs(),
            nextSections = setOf(SectionFactory.virreyDelPino1800_avCabildo20())
        )
        givenNextSections(
            from = LocationFactory.virreyDelPino_2880(),
            at = InstantFactory.october15_13hs(),
            nextSections = setOf(SectionFactory.virreyDelPino2880_avCabildo20())
        )

        // WHEN
        val result = searchTrip(
            SearchTrip.Input(
                departure = LocationFactory.avCabildo_4853(),
                arrival = LocationFactory.avCabildo_20(),
                at = InstantFactory.october15_12hs(),
            )
        )

        // THEN
        assertEquals(
            listOf(
                TripSearchPlanFactory.avCabildo4853_virreyDelPino1800_avCabildo20(),
                TripSearchPlanFactory.avCabildo4853_virreyDelPino2880_avCabildo20(),
            ),
            result
        )
    }

    @Test
    fun `given exists two trip plans with the same meeting point between the given departure and arrival then should return `() {
        // GIVEN
        circleSearchAreaRadius =
            circleSearchAreaRadius(LocationFactory.avCabildo_4853(), LocationFactory.avCabildo_20())
        givenNextSections(
            from = LocationFactory.avCabildo_4853(),
            at = InstantFactory.october15_12hs(),
            nextSections = setOf(
                SectionFactory.avCabildo4853_virreyDelPino1800(),
                SectionFactory.avCabildo4853_virreyDelPino2880()
            )
        )
        givenNextSections(
            from = LocationFactory.virreyDelPino_1800(),
            at = InstantFactory.october15_17hs(),
            nextSections = setOf(
                SectionFactory.virreyDelPino1800_avCabildo20(),
                SectionFactory.virreyDelPino1800_avDelLibertador5000()
            )
        )
        givenNextSections(
            from = LocationFactory.avDelLibertador_5000(),
            at = InstantFactory.october15_17_30hs(),
            nextSections = setOf(SectionFactory.avDelLibertador5000_avCabildo20())
        )
        givenNextSections(
            from = LocationFactory.virreyDelPino_2880(),
            at = InstantFactory.october15_13hs(),
            nextSections = setOf(SectionFactory.virreyDelPino2880_avCabildo20())
        )

        // WHEN
        val result = searchTrip(
            SearchTrip.Input(
                departure = LocationFactory.avCabildo_4853(),
                arrival = LocationFactory.avCabildo_20(),
                at = InstantFactory.october15_12hs(),
            )
        )

        // THEN
        assertEquals(
            listOf(
                TripSearchPlanFactory.avCabildo4853_virreyDelPino1800_avCabildo20(),
                TripSearchPlanFactory.avCabildo4853_virreyDelPino2880_avCabildo20(),
                TripSearchPlanFactory.avCabildo4853_virreyDelPino1800_avDelLibertador5000_avCabildo20(),
            ),
            result
        )
    }

    @Test
    fun `given an existing trip plan which match with the given departure and a near arrival location, then should return it`() {
        // GIVEN
        val departure = LocationFactory.avCabildo_4853()
        val arrival = LocationFactory.avCabildo_50()
        val at = InstantFactory.october15_12hs()
        circleSearchAreaRadius = circleSearchAreaRadius(departure, arrival)
        givenNextSections(
            from = departure,
            at = at,
            nextSections = setOf(SectionFactory.avCabildo())
        )

        // WHEN
        val result = searchTrip(
            SearchTrip.Input(
                departure = departure,
                arrival = arrival,
                at = at,
            )
        )

        // THEN
        assertEquals(listOf(TripSearchPlanFactory.avCabildo()), result)
    }

    private fun circleSearchAreaRadius(departure: Location, arrival: Location): Double =
        SearchRadiusGenerator(departure, arrival)

    private fun givenNextSections(from: Location, at: Instant, nextSections: Set<Section>) {
        every {
            sectionRepository.findNextSectionsIn(
                match { it == JtsCircleMother.common(from, circleSearchAreaRadius) },
                at
            )
        } returns nextSections
    }
}

