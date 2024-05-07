package com.roadlink.tripservice.domain.trip

import com.roadlink.tripservice.domain.trip.constraint.Policy
import com.roadlink.tripservice.domain.trip.constraint.Restriction
import com.roadlink.tripservice.domain.trip.constraint.Rule.*
import com.roadlink.tripservice.domain.trip.section.Section
import com.roadlink.tripservice.domain.trip_search.Filter
import com.roadlink.tripservice.domain.trip_search.Filter.*
import com.roadlink.tripservice.infrastructure.UUIDGenerator
import com.roadlink.tripservice.usecases.trip.TripFactory
import com.roadlink.tripservice.usecases.user.UserFactory
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class TripTest {

    private val idGenerator = UUIDGenerator()

    @Test
    fun `when a trip has a meeting point then 2 sections must be created`() {
        // given
        val trip = givenATripWithAMeetingPoint()

        // when
        val sections = trip.sections(idGenerator)

        // then
        thenTheExpectedAmountOfSectionsWereCreated(2, sections)
    }

    @Test
    fun `when a trip has 2 meeting points then 3 sections must be created`() {
        // given
        val trip = givenATripWithTwoMeetingPoint()

        // when
        val sections = trip.sections(idGenerator)

        // then
        thenTheExpectedAmountOfSectionsWereCreated(3, sections)
    }

    @ParameterizedTest
    @MethodSource("tripDataProvider")
    fun `when a trip has specific policies, then check compliance`(
        testName: String,
        policies: List<Policy>,
        restrictions: List<Restriction>,
        filters: List<Filter>,
        expectedResult: Boolean
    ) {
        // given
        val passenger = UserFactory.common(firstName = "Jorge", lastName = "Cabrera")
        val trip = TripFactory.common(policies = policies, restrictions = restrictions)

        // when
        val isCompliant = trip.isCompliant(passenger, filters)

        // then
        assertEquals(expectedResult, isCompliant)
    }

    companion object {
        @JvmStatic
        fun tripDataProvider(): Stream<Arguments> = Stream.of(
            Arguments.of(
                "when a trip has some rules, and the filter NO_SMOKING is present, then the trip is compliant",
                listOf(NoSmoking, PetAllowed),
                listOf<Restriction>(),
                listOf(NO_SMOKING),
                true
            ),
            Arguments.of(
                "when a trip more than one rule, and all the filters are present, then the trip is compliant",
                listOf(NoSmoking, OnlyWomen, PetAllowed),
                listOf<Restriction>(),
                listOf(ONLY_WOMEN, PET_ALLOWED, NO_SMOKING),
                true
            ),
            Arguments.of(
                "when a trip has some rules, and all the filters are present, then the trip is compliant",
                listOf(NoSmoking, PetAllowed),
                listOf<Restriction>(),
                listOf(NO_SMOKING, PET_ALLOWED),
                true
            ),
            Arguments.of(
                "when a trip has some rules, but the filter ONLY_WOMEN is not present, then the trip is not compliant",
                listOf(NoSmoking, PetAllowed),
                listOf<Restriction>(),
                listOf(NO_SMOKING, PET_ALLOWED, ONLY_WOMEN),
                false
            ),
            Arguments.of(
                "when a trip does not have any rule and the filter ONLY_WOMEN is not present, then the trip is not compliant",
                listOf<Filter>(),
                listOf<Restriction>(),
                listOf(NO_SMOKING, PET_ALLOWED, ONLY_WOMEN),
                false
            ),
            Arguments.of(
                "when a trip has just one rule, but the filter ONLY_WOMEN is not present, then the trip is not compliant",
                listOf(NoSmoking),
                listOf<Restriction>(),
                listOf(ONLY_WOMEN),
                false
            )
        )
    }


    private fun givenATripWithTwoMeetingPoint(): Trip {
        return TripFactory.caba_escobar_pilar_rosario(id = "81dcb088-4b7e-4956-a50a-52eee0dd5a0b")
    }

    private fun thenTheExpectedAmountOfSectionsWereCreated(expectedSections: Int, sections: Set<Section>) {
        assertEquals(expectedSections, sections.size)
    }

    private fun givenATripWithAMeetingPoint(): Trip {
        return TripFactory.avCabildo4853_virreyDelPino1800_avCabildo20(id = "81dcb088-4b7e-4956-a50a-52eee0dd5a0b")
    }
}