package com.roadlink.tripservice.domain.trip_search

import com.roadlink.tripservice.domain.common.Location

import com.roadlink.tripservice.domain.trip.section.SectionRepository
import java.time.Instant

class BruteForceSearchEngine(
    private val sectionRepository: SectionRepository,
    private val circleSearchAreaCreator: SearchAreaCreator<JtsCircle>,
    private val distanceOnEarthInMeters: DistanceOnEarthInMeters,
) : SearchEngine {
    override fun search(departure: Location, arrival: Location, at: Instant): List<TripSearchPlanResult> {
        val circleSearchAreaRadius = distanceOnEarthInMeters(departure, arrival) * 0.01
        val tripSearchPlanResults = mutableListOf<TripSearchPlanResult>()

        val stack = mutableListOf<TripSearchPlanResult>()
        val initialCircleSearchArea = circleSearchAreaCreator.from(departure, circleSearchAreaRadius)
        sectionRepository.findNextSectionsIn(initialCircleSearchArea.value, at).forEach { nextSection ->
            val tripSearchPlanResult = TripSearchPlanResult(listOf(nextSection))
            stack.add(tripSearchPlanResult)
        }

        while (stack.isNotEmpty()) {
            val actualTripPlan = stack.removeLast()
            val actualSection = actualTripPlan.last()

            if (actualSection.arrivesTo(arrival)) {
                tripSearchPlanResults.add(actualTripPlan)
            } else {
                val circleSearchArea = circleSearchAreaCreator.from(actualSection.arrival(), circleSearchAreaRadius)
                val estimatedArrivalTimeAtArrival = actualSection.estimatedArrivalTimeAtArrival()
                sectionRepository.findNextSectionsIn(circleSearchArea.value, estimatedArrivalTimeAtArrival).forEach { nextSection ->
                    stack.add(actualTripPlan + nextSection)
                }
            }
        }

        return tripSearchPlanResults.sortedBy { it.distance() }
    }
}
