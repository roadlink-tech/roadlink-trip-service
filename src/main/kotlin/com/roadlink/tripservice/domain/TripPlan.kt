package com.roadlink.tripservice.domain

data class TripPlan(val sections: List<Section>) {
    operator fun plus(section: Section): TripPlan {
        return TripPlan(sections + section)
    }

    fun last(): Section = sections.last()

    fun distance(): Double = sections.sumOf { it.distance() }
}