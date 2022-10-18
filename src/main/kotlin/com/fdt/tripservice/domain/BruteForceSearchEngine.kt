package com.fdt.tripservice.domain

import java.time.Instant

class BruteForceSearchEngine(
    private val sectionRepository: SectionRepository,
) : SearchEngine {
    override fun search(departure: Location, arrival: Location, at: Instant): List<TripPlan> {
        val tripPlans = mutableListOf<TripPlan>()

        val stack = mutableListOf<TripPlan>()
        sectionRepository.findNextSectionsFrom(departure).forEach { nextSection ->
            stack.add(TripPlan(listOf(nextSection)))
        }

        while (stack.isNotEmpty()) {
            val actualTripPlan = stack.removeLast()
            val actualSection = actualTripPlan.last()
            if (actualSection.arrivesTo(arrival)) {
                tripPlans.add(actualTripPlan)
            }
            sectionRepository.findNextSectionsFrom(actualSection.arrival()).forEach { nextSection ->
                stack.add(actualTripPlan + nextSection)
            }
        }

        return tripPlans.sortedBy { it.distance() }
    }
}