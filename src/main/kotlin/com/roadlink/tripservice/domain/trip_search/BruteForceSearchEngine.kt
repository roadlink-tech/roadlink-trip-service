package com.roadlink.tripservice.domain.trip_search

import com.roadlink.tripservice.domain.common.Location
import com.roadlink.tripservice.domain.trip.section.SectionRepository
import java.time.Instant

class BruteForceSearchEngine(
    private val sectionRepository: SectionRepository,
) : SearchEngine {
    override fun search(departure: Location, arrival: Location, at: Instant): List<TripPlan> {
        val tripPlans = mutableListOf<TripPlan>()

        val stack = mutableListOf<TripPlan>()
        sectionRepository.findNextSections(departure, at).forEach { nextSection ->
            stack.add(TripPlan(listOf(nextSection)))
        }

        while (stack.isNotEmpty()) {
            val actualTripPlan = stack.removeLast()
            val actualSection = actualTripPlan.last()
            if (actualSection.arrivesTo(arrival)) {
                tripPlans.add(actualTripPlan)
            }
            sectionRepository.findNextSections(actualSection.arrival(), at).forEach { nextSection ->
                stack.add(actualTripPlan + nextSection)
            }
        }

        return tripPlans.sortedBy { it.distance() }
    }
}