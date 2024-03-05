package com.roadlink.tripservice.domain.trip_application

import java.util.*

interface TripApplicationRepository {
    fun saveAll(tripApplications: List<TripPlanApplication.TripApplication>)
    fun findAllByDriverId(driverId: UUID): List<TripPlanApplication.TripApplication>
    fun findByTripId(tripId: UUID): List<TripPlanApplication.TripApplication>
    fun findBySectionId(sectionId: String): Set<TripPlanApplication.TripApplication>
    fun find(commandQuery: CommandQuery): List<TripPlanApplication.TripApplication>
    data class CommandQuery(
        val sectionId: String = "",
        val tripId: UUID? = null,
        val driverId: UUID? = null
    )
}
