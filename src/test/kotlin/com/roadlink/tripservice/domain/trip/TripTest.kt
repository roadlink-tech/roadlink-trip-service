package com.roadlink.tripservice.domain.trip

import com.roadlink.tripservice.domain.trip.constraint.Policy
import com.roadlink.tripservice.domain.trip.constraint.Restriction
import com.roadlink.tripservice.domain.trip.constraint.Rule.*
import com.roadlink.tripservice.domain.trip.constraint.Visibility.*
import com.roadlink.tripservice.domain.trip.section.Section
import com.roadlink.tripservice.domain.trip_search.Filter
import com.roadlink.tripservice.domain.trip_search.Filter.*
import com.roadlink.tripservice.domain.user.User
import com.roadlink.tripservice.infrastructure.UUIDGenerator
import com.roadlink.tripservice.usecases.trip.TripFactory
import com.roadlink.tripservice.usecases.user.UserFactory
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.*
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
        passengerGender: User.Gender,
        passengerAndDriverAreFriends: Boolean,
        policies: List<Policy>,
        restrictions: List<Restriction>,
        filters: List<Filter>,
        expectedResult: Boolean
    ) {
        // given
        val trip = TripFactory.common(policies = policies, restrictions = restrictions)
        val passenger = if (passengerAndDriverAreFriends) {
            UserFactory.common(
                firstName = "Jorge",
                lastName = "Cabrera",
                friendsIds = setOf(UUID.fromString(trip.driverId)),
                gender = passengerGender
            )
        } else {
            UserFactory.common(
                firstName = "Jorge",
                lastName = "Cabrera",
                gender = passengerGender
            )
        }

        // when
        val isCompliant = trip.isCompliant(passenger, filters)

        // then
        assertEquals(expectedResult, isCompliant)
    }

    companion object {
        @JvmStatic
        fun tripDataProvider(): Stream<Arguments> = Stream.of(
            Arguments.of(
                "when a trip does not have rules and restrictions, then the trip is compliant",
                User.Gender.None,
                false,
                listOf<Policy>(),
                listOf<Restriction>(),
                listOf<Filter>(),
                true
            ),
            Arguments.of(
                "when a trip has some rules, and the filter NO_SMOKING is present, then the trip is compliant",
                User.Gender.None,
                false,
                listOf(NoSmoking, PetAllowed),
                listOf<Restriction>(),
                listOf(NO_SMOKING),
                true
            ),
            Arguments.of(
                "given a trip with rules and ONLY_WOMEN restriction, when the filter NO_SMOKING is present but the passenger is a men, then the trip is not compliant",
                User.Gender.Male,
                false,
                listOf(NoSmoking, PetAllowed),
                listOf<Restriction>(OnlyWomen),
                listOf(NO_SMOKING),
                false
            ),
            Arguments.of(
                "given a trip with rules and ONLY_WOMEN restriction, when the filter NO_SMOKING is present but the passenger is a woman, then the trip is compliant",
                User.Gender.Female,
                false,
                listOf(NoSmoking, PetAllowed),
                listOf<Restriction>(OnlyWomen),
                listOf(NO_SMOKING),
                true
            ),
            Arguments.of(
                "given a trip with rules and ONLY_WOMEN, PRIVATE restrictions, when the passenger is a women but is not a driver friend, then the trip is not compliant",
                User.Gender.Female,
                false,
                listOf(NoSmoking, PetAllowed),
                listOf<Restriction>(OnlyWomen, Private),
                listOf(NO_SMOKING),
                false
            ),
            Arguments.of(
                "when a trip has some rules, and all the filters are present, then the trip is compliant",
                User.Gender.Male,
                false,
                listOf(NoSmoking, PetAllowed),
                listOf<Restriction>(),
                listOf(NO_SMOKING, PET_ALLOWED),
                true
            ),
            Arguments.of(
                "given a private trip with some rules, when passenger and driver are friends, then the trip is compliant",
                User.Gender.None,
                true,
                listOf(NoSmoking, PetAllowed),
                listOf(Private),
                listOf(PRIVATE),
                true
            ),

            Arguments.of(
                "given a private trip for women, when passenger and driver are friends and the passenger is a women, then the trip is compliant",
                User.Gender.Female,
                true,
                listOf(NoSmoking, PetAllowed),
                listOf(Private, OnlyWomen),
                listOf(PRIVATE, ONLY_WOMEN),
                true
            ),
            Arguments.of(
                "given a private trip with ONLY_WOMEN restriction, when passenger and driver are friends but passenger is a men, then the trip is not compliant",
                User.Gender.Male,
                true,
                listOf(NoSmoking, PetAllowed),
                listOf(Private, OnlyWomen),
                listOf(PRIVATE),
                false
            ),
            Arguments.of(
                "when a trip has some rules, but the restriction ONLY_WOMEN is not present, then the trip is not compliant",
                User.Gender.None,
                false,
                listOf(NoSmoking, PetAllowed),
                listOf<Restriction>(),
                listOf(NO_SMOKING, PET_ALLOWED, ONLY_WOMEN),
                false
            ),
            Arguments.of(
                "when the restriction ONLY_WOMEN is present, but the passenger is a men, then the trip is not compliant",
                User.Gender.Male,
                false,
                listOf(NoSmoking, PetAllowed),
                listOf<Restriction>(OnlyWomen),
                listOf(NO_SMOKING, PET_ALLOWED, ONLY_WOMEN),
                false
            ),
            Arguments.of(
                "when the restriction ONLY_WOMEN is present, and the passenger is a women, then the trip is compliant",
                User.Gender.Female,
                false,
                listOf(NoSmoking, PetAllowed),
                listOf<Restriction>(OnlyWomen),
                listOf(NO_SMOKING, ONLY_WOMEN),
                true
            ),
            Arguments.of(
                "when the rule NO_SMOKING is not present, then the trip is not compliant",
                User.Gender.Female,
                false,
                listOf(PetAllowed),
                listOf<Restriction>(OnlyWomen),
                listOf(NO_SMOKING, ONLY_WOMEN),
                false
            ),
            Arguments.of(
                "when a trip does not have any rule and the filter ONLY_WOMEN is not present, then the trip is not compliant",
                User.Gender.None,
                false,
                listOf<Filter>(),
                listOf<Restriction>(),
                listOf(NO_SMOKING, PET_ALLOWED, ONLY_WOMEN),
                false
            ),
            Arguments.of(
                "when a trip has just one rule, but the restriction ONLY_WOMEN is not present, then the trip is not compliant",
                User.Gender.None,
                false,
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