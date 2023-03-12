package com.roadlink.tripservice.domain

import com.roadlink.tripservice.domain.trip.TripPlan
import com.roadlink.tripservice.domain.trip.section.SectionRepository
import java.time.Instant

class BruteForceSearchEngine(
    private val sectionRepository: SectionRepository,
) : com.roadlink.tripservice.domain.SearchEngine {
    override fun search(departure: com.roadlink.tripservice.domain.Location, arrival: com.roadlink.tripservice.domain.Location, at: Instant): List<TripPlan> {
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