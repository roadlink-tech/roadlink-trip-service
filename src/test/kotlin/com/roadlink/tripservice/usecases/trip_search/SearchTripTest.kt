package com.roadlink.tripservice.usecases.trip_search

import com.roadlink.tripservice.domain.common.Location
import com.roadlink.tripservice.domain.trip.TripRepository
import com.roadlink.tripservice.domain.trip.section.Section
import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.domain.trip_search.JtsSearchCircleCreator
import com.roadlink.tripservice.domain.trip_search.SearchRadiusGenerator
import com.roadlink.tripservice.domain.trip_search.algorithm.BruteForceSearchEngine
import com.roadlink.tripservice.domain.trip_search.filter.FilterService
import com.roadlink.tripservice.domain.trip_search.filter.SearchFilterService
import com.roadlink.tripservice.domain.user.UserRepository
import com.roadlink.tripservice.usecases.common.InstantFactory
import com.roadlink.tripservice.usecases.common.address.LocationFactory
import com.roadlink.tripservice.usecases.trip.SectionFactory
import com.roadlink.tripservice.usecases.trip.TripFactory
import com.roadlink.tripservice.usecases.user.UserFactory
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.*
import kotlin.properties.Delegates

internal class SearchTripTest {

    private lateinit var sectionRepository: SectionRepository

    private lateinit var userRepository: UserRepository

    private lateinit var tripRepository: TripRepository

    private lateinit var filterService: FilterService

    private var circleSearchAreaRadius by Delegates.notNull<Double>()

    private lateinit var searchTrip: SearchTrip

    @BeforeEach
    fun setUp() {
        sectionRepository = mockk()
        userRepository = mockk()
        tripRepository = mockk()
        filterService = SearchFilterService(tripRepository)

        val bruteForceSearchEngine = BruteForceSearchEngine(
            sectionRepository = sectionRepository,
            circleSearchAreaCreator = JtsSearchCircleCreator(),
        )

        searchTrip = SearchTrip(
            userRepository = userRepository,
            searchEngine = bruteForceSearchEngine,
            filterService = filterService
        )
    }

    @Test
    fun `given no existing section then should return an empty list`() {
        // GIVEN
        val user = UserFactory.common()
        val departure = LocationFactory.avCabildo_4853()
        val arrival = LocationFactory.avCabildo_20()
        val at = InstantFactory.october15_12hs()
        val matchedTrip = TripFactory.common()

        every { userRepository.findByUserId(id = match { it == user.id }) } returns user
        every {
            tripRepository.find(commandQuery = match {
                it.ids.contains(
                    UUID.fromString(
                        matchedTrip.id
                    )
                )
            })
        } returns listOf(matchedTrip)

        circleSearchAreaRadius = circleSearchAreaRadius(departure, arrival)
        givenNextSections(from = departure, at = at, nextSections = emptySet())

        // WHEN
        val result = searchTrip(
            SearchTrip.Input(
                departure = departure,
                arrival = arrival,
                at = at,
                callerId = UUID.fromString(user.id)
            )
        ).result

        // THEN
        assertTrue(result.isEmpty())
    }

    @Test
    fun `given existing trip plan between the given departure and arrival parameters, then should return it`() {
        // GIVEN
        val user = UserFactory.common()
        val departure = LocationFactory.avCabildo_4853()
        val arrival = LocationFactory.avCabildo_20()
        val at = InstantFactory.october15_12hs()
        val matchedTrip = TripFactory.common()

        every { userRepository.findByUserId(id = match { it == user.id }) } returns user
        every {
            tripRepository.find(commandQuery = match {
                it.ids.contains(
                    UUID.fromString(
                        matchedTrip.id
                    )
                )
            })
        } returns listOf(matchedTrip)

        circleSearchAreaRadius = circleSearchAreaRadius(departure, arrival)
        givenNextSections(
            from = departure,
            at = at,
            nextSections = setOf(SectionFactory.avCabildo(tripId = UUID.fromString(matchedTrip.id)))
        )

        // WHEN
        val result = searchTrip(
            SearchTrip.Input(
                departure = departure,
                arrival = arrival,
                at = at,
                callerId = UUID.fromString(user.id)
            )
        ).result

        // THEN
        assertEquals(
            listOf(
                TripSearchPlanResultFactory.avCabildo(
                    tripId = UUID.fromString(
                        matchedTrip.id
                    )
                )
            ), result
        )
    }

    @Test
    fun `given an existing trip plan with one meeting point between the given departure and arrival parameter, then should return it`() {
        // GIVEN
        val user = UserFactory.common()
        val matchedTrip = TripFactory.common()
        circleSearchAreaRadius =
            circleSearchAreaRadius(LocationFactory.avCabildo_4853(), LocationFactory.avCabildo_20())
        givenNextSections(
            from = LocationFactory.avCabildo_4853(),
            at = InstantFactory.october15_12hs(),
            nextSections = setOf(
                SectionFactory.avCabildo4853_virreyDelPino1800(
                    tripId = UUID.fromString(
                        matchedTrip.id
                    )
                )
            )
        )
        givenNextSections(
            from = LocationFactory.virreyDelPino_1800(),
            at = InstantFactory.october15_17hs(),
            nextSections = setOf(
                SectionFactory.virreyDelPino1800_avCabildo20(
                    tripId = UUID.fromString(
                        matchedTrip.id
                    )
                )
            )
        )

        every { userRepository.findByUserId(id = match { it == user.id }) } returns user
        every {
            tripRepository.find(commandQuery = match {
                it.ids.contains(
                    UUID.fromString(
                        matchedTrip.id
                    )
                )
            })
        } returns listOf(matchedTrip)

        // WHEN
        val result = searchTrip(
            SearchTrip.Input(
                departure = LocationFactory.avCabildo_4853(),
                arrival = LocationFactory.avCabildo_20(),
                at = InstantFactory.october15_12hs(),
                callerId = UUID.fromString(user.id)
            )
        ).result

        // THEN
        assertEquals(
            listOf(
                TripSearchPlanResultFactory.avCabildo4853_virreyDelPino1800_avCabildo20(
                    tripId = UUID.fromString(
                        matchedTrip.id
                    )
                )
            ), result
        )
    }

    @Test
    fun `given two existing trip plans with one meeting point between the given departure and arrival, then the shorter must be first`() {
        // GIVEN
        val user = UserFactory.common()
        val matchedTrip = TripFactory.common()
        circleSearchAreaRadius =
            circleSearchAreaRadius(LocationFactory.avCabildo_4853(), LocationFactory.avCabildo_20())
        givenNextSections(
            from = LocationFactory.avCabildo_4853(),
            at = InstantFactory.october15_12hs(),
            nextSections = setOf(
                SectionFactory.avCabildo4853_virreyDelPino1800(
                    tripId = UUID.fromString(
                        matchedTrip.id
                    )
                ), SectionFactory.avCabildo4853_virreyDelPino2880(
                    tripId = UUID.fromString(
                        matchedTrip.id
                    )
                )
            )
        )
        givenNextSections(
            from = LocationFactory.virreyDelPino_1800(),
            at = InstantFactory.october15_17hs(),
            nextSections = setOf(
                SectionFactory.virreyDelPino1800_avCabildo20(
                    tripId = UUID.fromString(
                        matchedTrip.id
                    )
                )
            )
        )
        givenNextSections(
            from = LocationFactory.virreyDelPino_2880(),
            at = InstantFactory.october15_13hs(),
            nextSections = setOf(
                SectionFactory.virreyDelPino2880_avCabildo20(
                    tripId = UUID.fromString(
                        matchedTrip.id
                    )
                )
            )
        )

        every { userRepository.findByUserId(id = match { it == user.id }) } returns user
        every {
            tripRepository.find(commandQuery = match {
                it.ids.contains(
                    UUID.fromString(
                        matchedTrip.id
                    )
                )
            })
        } returns listOf(matchedTrip)

        // WHEN
        val result = searchTrip(
            SearchTrip.Input(
                departure = LocationFactory.avCabildo_4853(),
                arrival = LocationFactory.avCabildo_20(),
                at = InstantFactory.october15_12hs(),
                callerId = UUID.fromString(user.id)
            )
        ).result

        // THEN
        assertEquals(
            listOf(
                TripSearchPlanResultFactory.avCabildo4853_virreyDelPino1800_avCabildo20(
                    tripId = UUID.fromString(
                        matchedTrip.id
                    )
                ),
                TripSearchPlanResultFactory.avCabildo4853_virreyDelPino2880_avCabildo20(
                    tripId = UUID.fromString(
                        matchedTrip.id
                    )
                ),
            ), result
        )
    }

    @Test
    fun `given two existing trip plans with the same meeting point between the given departure and arrival then should return `() {
        // GIVEN
        val user = UserFactory.common()
        val oneTrip = TripFactory.common()
        circleSearchAreaRadius =
            circleSearchAreaRadius(LocationFactory.avCabildo_4853(), LocationFactory.avCabildo_20())
        givenNextSections(
            from = LocationFactory.avCabildo_4853(),
            at = InstantFactory.october15_12hs(),
            nextSections = setOf(
                SectionFactory.avCabildo4853_virreyDelPino1800(tripId = UUID.fromString(oneTrip.id)),
                SectionFactory.avCabildo4853_virreyDelPino2880(tripId = UUID.fromString(oneTrip.id))
            )
        )
        givenNextSections(
            from = LocationFactory.virreyDelPino_1800(),
            at = InstantFactory.october15_17hs(),
            nextSections = setOf(
                SectionFactory.virreyDelPino1800_avCabildo20(tripId = UUID.fromString(oneTrip.id)),
                SectionFactory.virreyDelPino1800_avDelLibertador5000(
                    tripId = UUID.fromString(
                        oneTrip.id
                    )
                )
            )
        )
        givenNextSections(
            from = LocationFactory.avDelLibertador_5000(),
            at = InstantFactory.october15_17_30hs(),
            nextSections = setOf(
                SectionFactory.avDelLibertador5000_avCabildo20(
                    tripId = UUID.fromString(
                        oneTrip.id
                    )
                )
            )
        )
        givenNextSections(
            from = LocationFactory.virreyDelPino_2880(),
            at = InstantFactory.october15_13hs(),
            nextSections = setOf(
                SectionFactory.virreyDelPino2880_avCabildo20(
                    tripId = UUID.fromString(
                        oneTrip.id
                    )
                )
            )
        )

        every { userRepository.findByUserId(id = match { it == user.id }) } returns user
        every {
            tripRepository.find(commandQuery = match {
                it.ids.contains(
                    UUID.fromString(
                        oneTrip.id
                    )
                )
            })
        } returns listOf(oneTrip)

        // WHEN
        val result = searchTrip(
            SearchTrip.Input(
                departure = LocationFactory.avCabildo_4853(),
                arrival = LocationFactory.avCabildo_20(),
                at = InstantFactory.october15_12hs(),
                callerId = UUID.fromString(user.id)
            )
        ).result

        // THEN
        assertEquals(
            listOf(
                TripSearchPlanResultFactory.avCabildo4853_virreyDelPino1800_avCabildo20(
                    tripId = UUID.fromString(
                        oneTrip.id
                    )
                ),
                TripSearchPlanResultFactory.avCabildo4853_virreyDelPino2880_avCabildo20(
                    tripId = UUID.fromString(
                        oneTrip.id
                    )
                ),
                TripSearchPlanResultFactory.avCabildo4853_virreyDelPino1800_avDelLibertador5000_avCabildo20(
                    tripId = UUID.fromString(oneTrip.id)
                ),
            ),
            result
        )
    }

    @Test
    fun `given an existing trip plan which match with the given departure and a near arrival location, then should return it`() {
        // GIVEN
        val user = UserFactory.common()
        val oneTrip = TripFactory.common()
        val departure = LocationFactory.avCabildo_4853()
        val arrival = LocationFactory.avCabildo_50()
        val at = InstantFactory.october15_12hs()
        circleSearchAreaRadius = circleSearchAreaRadius(departure, arrival)
        givenNextSections(
            from = departure,
            at = at,
            nextSections = setOf(SectionFactory.avCabildo(tripId = UUID.fromString(oneTrip.id)))
        )

        every { userRepository.findByUserId(id = match { it == user.id }) } returns user
        every {
            tripRepository.find(commandQuery = match {
                it.ids.contains(
                    UUID.fromString(
                        oneTrip.id
                    )
                )
            })
        } returns listOf(oneTrip)

        // WHEN
        val result = searchTrip(
            SearchTrip.Input(
                departure = departure,
                arrival = arrival,
                at = at,
                callerId = UUID.fromString(user.id)
            )
        ).result

        // THEN
        assertEquals(
            listOf(TripSearchPlanResultFactory.avCabildo(tripId = UUID.fromString(oneTrip.id))),
            result
        )
    }

    private fun circleSearchAreaRadius(departure: Location, arrival: Location): Double =
        SearchRadiusGenerator(departure, arrival)

    private fun givenNextSections(from: Location, at: Instant, nextSections: Set<Section>) {
        every {
            sectionRepository.findNextSectionsIn(
                match { it == JtsCircleMother.common(from, circleSearchAreaRadius) }, at
            )
        } returns nextSections
    }
}

