package com.roadlink.tripservice.domain.trip_search

import com.roadlink.tripservice.domain.common.Location

import com.roadlink.tripservice.domain.trip.section.SectionRepository
import java.time.Instant

class BruteForceSearchEngine(
    private val sectionRepository: SectionRepository,
) : SearchEngine {
    override fun search(departure: Location, arrival: Location, at: Instant): List<TripSearchPlanResult> {
        val tripSearchPlanResults = mutableListOf<TripSearchPlanResult>()

        val stack = mutableListOf<TripSearchPlanResult>()
        sectionRepository.findNextSections(departure, at).forEach { nextSection ->
            stack.add(TripSearchPlanResult(listOf(nextSection)))
        }

        while (stack.isNotEmpty()) {
            val actualTripPlan = stack.removeLast()
            val actualSection = actualTripPlan.last()
            if (actualSection.arrivesTo(arrival)) {
                tripSearchPlanResults.add(actualTripPlan)
            }
            sectionRepository.findNextSections(actualSection.arrival(), at).forEach { nextSection ->
                stack.add(actualTripPlan + nextSection)
            }
        }

        return tripSearchPlanResults.sortedBy { it.distance() }
    }
}