package com.fdt.tripservice.usecases

import com.fdt.tripservice.domain.trip.Location
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class SearchTripTest {
    @Test
    fun `foo bar`() {
        // given
        val locationA = Location(-34.568683, -58.450998, "A")
        val locationB = Location(-34.568409, -58.448209, "B")
        val locationC = Location(-34.573860, -58.452533, "C")
        //val locationC = Location(-34.571104, -58.450366, "C")
        val locationD = Location(-34.570565, -58.447630, "D")
        val fakeNextLocationsFrom = FakeNextLocationsFrom(
            nexts = mutableMapOf(
                locationA to setOf(locationB, locationC),
                locationB to setOf(locationD),
                locationC to setOf(locationD),
            ),
        )
        val pickWithProbability = PickWithProbability(RandomUniformDoubleGenerator())
        val searchTrip = SearchTrip(
            nextLocationsFrom = fakeNextLocationsFrom,
            pickWithProbability = pickWithProbability,
        )

        // when
        searchTrip(
            SearchTrip.Request(
            departure = locationA,
            arrival = locationD,
        ))

    }
}