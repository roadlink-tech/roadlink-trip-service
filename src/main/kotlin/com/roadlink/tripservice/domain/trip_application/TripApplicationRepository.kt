package com.roadlink.tripservice.domain.trip_application

import java.util.*

interface TripApplicationRepository {
    fun saveAll(tripApplications: List<TripPlanApplication.TripApplication>)
    fun findAllByDriverId(driverId: UUID): List<TripPlanApplication.TripApplication>
}