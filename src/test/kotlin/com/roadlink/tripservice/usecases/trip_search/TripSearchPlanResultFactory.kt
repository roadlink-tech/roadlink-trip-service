package com.roadlink.tripservice.usecases.trip_search

import com.roadlink.tripservice.domain.trip_search.TripSearchPlanResult
import com.roadlink.tripservice.usecases.trip.SectionFactory
import java.util.*

object TripSearchPlanResultFactory {

    fun common(tripId: UUID = UUID.randomUUID()): TripSearchPlanResult {
        return TripSearchPlanResult(sections = listOf(SectionFactory.avCabildo(tripId = tripId)))
    }

    fun avCabildo(tripId: UUID = UUID.fromString("ad2e7d11-0fbb-4711-8f0c-a0f31529241f")) =
        TripSearchPlanResult(sections = listOf(SectionFactory.avCabildo(tripId = tripId)))

    fun avCabildo4853_virreyDelPino1800_avCabildo20(tripId: UUID = UUID.fromString("bd7ee293-f5d3-4832-a74c-17e9a8fa465f")) =
        TripSearchPlanResult(
            sections = listOf(
                SectionFactory.avCabildo4853_virreyDelPino1800(tripId = tripId),
                SectionFactory.virreyDelPino1800_avCabildo20(tripId = tripId),
            )
        )

    fun avCabildo4853_virreyDelPino2880_avCabildo20(tripId: UUID = UUID.fromString("bd7ee293-f5d3-4832-a74c-17e9a8fa465f")) =
        TripSearchPlanResult(
            sections = listOf(
                SectionFactory.avCabildo4853_virreyDelPino2880(tripId = tripId),
                SectionFactory.virreyDelPino2880_avCabildo20(tripId = tripId),
            )
        )

    fun avCabildo4853_virreyDelPino1800_avDelLibertador5000_avCabildo20(
        tripId: UUID = UUID.fromString(
            "bd7ee293-f5d3-4832-a74c-17e9a8fa465f"
        )
    ) =
        TripSearchPlanResult(
            sections = listOf(
                SectionFactory.avCabildo4853_virreyDelPino1800(tripId = tripId),
                SectionFactory.virreyDelPino1800_avDelLibertador5000(tripId = tripId),
                SectionFactory.avDelLibertador5000_avCabildo20(tripId = tripId),
            )
        )
}