package com.roadlink.tripservice.domain.trip_search.algorithm

import com.roadlink.tripservice.domain.common.Location

import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.domain.trip_search.JtsCircle
import com.roadlink.tripservice.domain.trip_search.SearchAreaCreator
import com.roadlink.tripservice.domain.trip_search.SearchRadiusGenerator
import com.roadlink.tripservice.domain.trip_search.TripSearchPlanResult
import java.time.Instant

class BruteForceSearchEngine(
    private val sectionRepository: SectionRepository,
    private val circleSearchAreaCreator: SearchAreaCreator<JtsCircle>,
) : SearchEngine {

    override fun search(
        departure: Location,
        arrival: Location,
        at: Instant
    ): List<TripSearchPlanResult> {
        val circleSearchAreaRadius = SearchRadiusGenerator(departure, arrival)
        val tripSearchPlanResults = mutableListOf<TripSearchPlanResult>()
        val stack = mutableListOf<TripSearchPlanResult>()
        val initialDepartureSearchArea =
            circleSearchAreaCreator.from(departure, circleSearchAreaRadius)
        sectionRepository.findNextSectionsIn(initialDepartureSearchArea.value, at)
            .forEach { nextSection ->
                val tripSearchPlanResult = TripSearchPlanResult(listOf(nextSection))
                stack.add(tripSearchPlanResult)
            }

        while (stack.isNotEmpty()) {
            val actualTripPlan = stack.removeLast()
            val actualSection = actualTripPlan.last()

            val arrivalSearchArea =
                circleSearchAreaCreator.from(arrival, circleSearchAreaRadius)
            if (arrivalSearchArea.contains(actualSection.arrival())) {
                tripSearchPlanResults.add(actualTripPlan)
            } else {
                val departureSearchArea =
                    circleSearchAreaCreator.from(actualSection.arrival(), circleSearchAreaRadius)
                val estimatedArrivalTimeAtArrival = actualSection.estimatedArrivalTimeAtArrival()
                sectionRepository.findNextSectionsIn(
                    departureSearchArea.value,
                    estimatedArrivalTimeAtArrival
                ).forEach { nextSection ->
                    stack.add(actualTripPlan + nextSection)
                }
            }
        }

        return tripSearchPlanResults.sortedBy { it.distance() }
    }
}
