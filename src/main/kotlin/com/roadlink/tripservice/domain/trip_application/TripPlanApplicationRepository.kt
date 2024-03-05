package com.roadlink.tripservice.domain.trip_application

import java.util.*

interface TripPlanApplicationRepository {
    fun insert(tripPlanApplication: TripPlanApplication)
    fun update(tripPlanApplication: TripPlanApplication)
    fun findByTripApplicationId(tripApplicationId: UUID): TripPlanApplication?
    fun findById(id: UUID): TripPlanApplication?
    fun findAllByPassengerId(passengerId: UUID): List<TripPlanApplication>
}