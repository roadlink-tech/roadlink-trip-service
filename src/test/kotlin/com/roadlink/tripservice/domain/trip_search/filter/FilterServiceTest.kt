package com.roadlink.tripservice.domain.trip_search.filter

import com.roadlink.tripservice.domain.trip.TripRepository
import com.roadlink.tripservice.domain.trip.constraint.Policy
import com.roadlink.tripservice.domain.trip.constraint.Restriction
import com.roadlink.tripservice.domain.trip.constraint.Rule
import com.roadlink.tripservice.domain.trip.constraint.Visibility
import com.roadlink.tripservice.domain.user.User
import com.roadlink.tripservice.usecases.trip.TripFactory
import com.roadlink.tripservice.usecases.trip_search.TripSearchPlanResultFactory
import com.roadlink.tripservice.usecases.user.UserFactory
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.*
import java.util.stream.*

class FilterServiceTest {

    @MockK
    lateinit var tripRepository: TripRepository

    private lateinit var filterService: FilterService

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        filterService = SearchFilterService(tripRepository)
    }

    @ParameterizedTest
    @MethodSource("dataProvider")
    fun `when a trip has specific policies, then check if it must be filtered`(
        testName: String,
        passengerGender: User.Gender,
        passengerAndDriverAreFriends: Boolean,
        policies: List<Policy>,
        restrictions: List<Restriction>,
        filters: Set<Filter>,
        shouldBeFiltered: Boolean
    ) {
        // given
        val trip = TripFactory.common(policies = policies, restrictions = restrictions)
        val result = listOf(TripSearchPlanResultFactory.common(tripId = UUID.fromString(trip.id)))
        val george = if (passengerAndDriverAreFriends) {
            UserFactory.common(
                gender = passengerGender,
                friendsIds = setOf(UUID.fromString(trip.driverId))
            )
        } else {
            UserFactory.common(gender = passengerGender)
        }
        every { tripRepository.find(commandQuery = match { it.ids.contains(UUID.fromString(trip.id)) }) } returns listOf(
            trip
        )

        // when
        val filteredResult = filterService.evaluate(george, result, filters)

        // then
        Assertions.assertEquals(shouldBeFiltered, result != filteredResult)
    }

    companion object {
        @JvmStatic
        fun dataProvider(): Stream<Arguments> = Stream.of(
            Arguments.of(
                "when the search returns a public trip without any rule or restrictions and the filters are empty, then it must not be filtered",
                User.Gender.X,
                false,
                listOf<Policy>(),
                listOf<Restriction>(),
                setOf<Filter>(),
                false
            ),
            Arguments.of(
                "when the search returns a public trip for women without any rule, the filters are empty and the caller is a woman, then it must not be filtered",
                User.Gender.Female,
                false,
                listOf<Policy>(),
                listOf<Restriction>(Visibility.OnlyWomen),
                setOf<Filter>(),
                false
            ),
            Arguments.of(
                "when the search returns a private trip for women without any rule, the filters are empty and the caller is a woman but not a driver fried, then it must be filtered",
                User.Gender.Female,
                false,
                listOf<Policy>(),
                listOf<Restriction>(Visibility.OnlyWomen, Visibility.Private),
                setOf<Filter>(),
                true
            ),
            Arguments.of(
                "when the search returns a private trip for women without any rule, the filters are empty and the caller is a woman who is a driver fried, then it must not be filtered",
                User.Gender.Female,
                true,
                listOf<Policy>(),
                listOf<Restriction>(Visibility.OnlyWomen, Visibility.Private),
                setOf<Filter>(),
                false
            ),
            Arguments.of(
                "when the search returns a private trip for women without any rule, the filters are empty and the caller is a men who is a driver fried, then it must be filtered",
                User.Gender.Male,
                true,
                listOf<Policy>(),
                listOf<Restriction>(Visibility.OnlyWomen, Visibility.Private),
                setOf<Filter>(),
                true
            ),
            Arguments.of(
                "when the search returns a public trip which allow pets, the filter includes PET_ALLOWED, then it must not be filtered",
                User.Gender.Male,
                false,
                listOf<Policy>(Rule.PetAllowed),
                listOf<Restriction>(),
                setOf(Filter.PET_ALLOWED),
                false
            ),
            Arguments.of(
                "when the search returns a public trip without any rule, the filter includes PET_ALLOWED, then it must be filtered",
                User.Gender.Male,
                false,
                listOf<Policy>(),
                listOf<Restriction>(),
                setOf(Filter.PET_ALLOWED),
                true
            ),
            Arguments.of(
                "when the search returns a private trip which allow pets, the filter includes PET_ALLOWED but user and driver are not friends, then it must be filtered",
                User.Gender.Male,
                false,
                listOf<Policy>(Rule.PetAllowed),
                listOf<Restriction>(Visibility.Private),
                setOf(Filter.PET_ALLOWED),
                true
            ),
            Arguments.of(
                "when the search returns a private trip which allow pets and does not allow smoke, the filter includes PET_ALLOWED and user is a driver friend, then it must not be filtered",
                User.Gender.Male,
                true,
                listOf<Policy>(Rule.PetAllowed, Rule.NoSmoking),
                listOf<Restriction>(Visibility.Private),
                setOf(Filter.PET_ALLOWED),
                false
            ),
            Arguments.of(
                "when the search returns a public trip which allow pets and does not allow smoke, the filter includes PRIVATE, then it must be filtered",
                User.Gender.Male,
                true,
                listOf<Policy>(Rule.PetAllowed, Rule.NoSmoking),
                listOf<Restriction>(),
                setOf(Filter.PRIVATE),
                true
            ),
            Arguments.of(
                "when the search returns a private trip for women which allow pets and does not allow smoke, the filter includes PRIVATE, ONLY_WOMEN, NO_SMOKE and ALLOW_PETS, then it must not be filtered",
                User.Gender.Male,
                true,
                listOf<Policy>(Rule.PetAllowed, Rule.NoSmoking),
                listOf<Restriction>(Visibility.Private, Visibility.OnlyWomen),
                setOf(Filter.PRIVATE),
                true
            ),
        )
    }
}