package com.roadlink.tripservice.domain.trip_search.filter

import com.roadlink.tripservice.domain.trip.TripRepository
import com.roadlink.tripservice.domain.trip.constraint.Policy
import com.roadlink.tripservice.domain.trip.constraint.Restriction
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
import java.util.stream.Stream

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
        filters: List<Filter>,
        shouldBeFiltered: Boolean
    ) {
        // given
        val trip = TripFactory.common(policies = policies, restrictions = restrictions)
        val george = UserFactory.common(gender = passengerGender)
        val result = listOf(TripSearchPlanResultFactory.common(tripId = UUID.fromString(trip.id)))
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
                "when the search returns a public trip without any rule or restrictions and the filters are empty, then it must not be filter",
                User.Gender.X,
                false,
                listOf<Policy>(),
                listOf<Restriction>(),
                listOf<Filter>(),
                false
            ),
            Arguments.of(
                "when the search returns a public trip for women without any rule or restrictions, the filters are empty and the caller is a woman, then it must not be filter",
                User.Gender.Female,
                false,
                listOf<Policy>(),
                listOf<Restriction>(Visibility.OnlyWomen),
                listOf<Filter>(),
                false
            ),
        )
    }
}