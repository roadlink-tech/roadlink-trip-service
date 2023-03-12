package com.roadlink.tripservice.trip.domain

import com.roadlink.tripservice.domain.trip.TripPlan

object TripPlanFactory {
    fun avCabildo() = TripPlan(sections = listOf(SectionFactory.avCabildo()))

    fun avCabildo4853_virreyDelPino1800_avCabildo20() =
        TripPlan(sections = listOf(
            SectionFactory.avCabildo4853_virreyDelPino1800(),
            SectionFactory.virreyDelPino1800_avCabildo20(),
        ))

    fun avCabildo4853_virreyDelPino2880_avCabildo20() =
        TripPlan(sections = listOf(
            SectionFactory.avCabildo4853_virreyDelPino2880(),
            SectionFactory.virreyDelPino2880_avCabildo20(),
        ))

    fun avCabildo4853_virreyDelPino1800_avDelLibertador5000_avCabildo20() =
        TripPlan(sections = listOf(
            SectionFactory.avCabildo4853_virreyDelPino1800(),
            SectionFactory.virreyDelPino1800_avDelLibertador5000(),
            SectionFactory.avDelLibertador5000_avCabildo20(),
        ))
}