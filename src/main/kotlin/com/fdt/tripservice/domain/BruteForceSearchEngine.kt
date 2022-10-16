package com.fdt.tripservice.domain

import java.time.Instant

class BruteForceSearchEngine(
    private val sectionRepository: SectionRepository,
) : SearchEngine {
    override fun search(departure: Location, arrival: Location, at: Instant): List<TripPlan> {
        val tripPlans = mutableListOf<TripPlan>()

        val stack = mutableListOf<Section>()
        sectionRepository.findNextSectionsFrom(departure).forEach { nextSection ->
            stack.add(nextSection)
        }

        val sections = mutableListOf<Section>()
        while (stack.isNotEmpty()) {
            val actual = stack.removeLast()
            sections.add(actual)
            if (actual.arrivesTo(arrival)) {
                tripPlans.add(TripPlan(sections = sections.toList()))
                sections.clear()
            }
            sectionRepository.findNextSectionsFrom(actual.arrival()).forEach { nextSection ->
                stack.add(nextSection)
            }
        }

        return tripPlans.sortedBy { it.distance() }
    }
}