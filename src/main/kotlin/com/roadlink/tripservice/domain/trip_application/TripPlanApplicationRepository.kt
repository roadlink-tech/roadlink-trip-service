package com.roadlink.tripservice.domain.trip_application

import java.util.*

interface TripPlanApplicationRepository {
    fun insert(application: TripPlanApplication)
    fun update(application: TripPlanApplication)
    fun findByTripApplicationId(tripApplicationId: UUID): TripPlanApplication?
    fun findById(id: UUID): TripPlanApplication?
    // TODO move this behave to a TripApplicationRepository
    fun findTripApplicationBySectionId(sectionId: String): Set<TripPlanApplication.TripApplication>
}