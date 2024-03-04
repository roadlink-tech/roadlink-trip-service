package com.roadlink.tripservice.domain.trip_application

import java.util.*

interface TripPlanApplicationRepository {
    fun insert(application: TripPlanApplication)
    fun update(application: TripPlanApplication)
    fun findByTripApplicationId(tripApplicationId: UUID): TripPlanApplication?
    // TODO move this behave to a TripApplicationRepository
    fun findBySectionId(sectionId: String): Set<TripPlanApplication.TripApplication>
}