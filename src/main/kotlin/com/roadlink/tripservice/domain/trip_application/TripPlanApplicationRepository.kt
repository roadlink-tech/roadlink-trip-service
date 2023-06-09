package com.roadlink.tripservice.domain.trip_application

import java.util.*

interface TripPlanApplicationRepository {
    fun save(application: TripPlanApplication)
    fun findByTripApplicationId(tripApplicationId: UUID): TripPlanApplication?
    fun findTripApplicationBySectionId(sectionId: String): Set<TripPlanApplication.TripApplication>
}