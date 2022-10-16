package com.fdt.tripservice.domain

data class TripPlan(val sections: List<Section>) {
    fun distance(): Double = sections.sumOf { it.distance() }
}