package com.roadlink.tripservice.domain.trip_plan

/**
 * when all the trip leg solicitudes are confirmed, then the trip plan solicitude is confirmed and a trip plan can be created
 */
interface TripPlanRepository {
    fun insert(tripPlan: TripPlan): TripPlan
}