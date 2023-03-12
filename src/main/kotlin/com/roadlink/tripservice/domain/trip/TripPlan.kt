package com.roadlink.tripservice.domain.trip

import com.roadlink.tripservice.domain.trip.section.Section

data class TripPlan(val sections: List<Section>) {
    operator fun plus(section: Section): TripPlan {
        return TripPlan(sections + section)
    }

    fun last(): Section = sections.last()

    fun distance(): Double = sections.sumOf { it.distance() }
}