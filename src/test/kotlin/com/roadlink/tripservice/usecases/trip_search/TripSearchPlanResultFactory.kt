package com.roadlink.tripservice.usecases.trip_search

import com.roadlink.tripservice.domain.trip_search.TripSearchPlanResult
import com.roadlink.tripservice.usecases.trip.SectionFactory
import java.util.UUID

object TripSearchPlanResultFactory {

    fun common(tripId: UUID = UUID.randomUUID()): TripSearchPlanResult {
        return TripSearchPlanResult(sections = listOf(SectionFactory.avCabildo(tripId = tripId)))
    }

    fun avCabildo() = TripSearchPlanResult(sections = listOf(SectionFactory.avCabildo()))

    fun avCabildo4853_virreyDelPino1800_avCabildo20() =
        TripSearchPlanResult(
            sections = listOf(
                SectionFactory.avCabildo4853_virreyDelPino1800(),
                SectionFactory.virreyDelPino1800_avCabildo20(),
            )
        )

    fun avCabildo4853_virreyDelPino2880_avCabildo20() =
        TripSearchPlanResult(
            sections = listOf(
                SectionFactory.avCabildo4853_virreyDelPino2880(),
                SectionFactory.virreyDelPino2880_avCabildo20(),
            )
        )

    fun avCabildo4853_virreyDelPino1800_avDelLibertador5000_avCabildo20() =
        TripSearchPlanResult(
            sections = listOf(
                SectionFactory.avCabildo4853_virreyDelPino1800(),
                SectionFactory.virreyDelPino1800_avDelLibertador5000(),
                SectionFactory.avDelLibertador5000_avCabildo20(),
            )
        )
}