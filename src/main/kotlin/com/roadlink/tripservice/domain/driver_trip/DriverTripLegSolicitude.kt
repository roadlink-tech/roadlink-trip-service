package com.roadlink.tripservice.domain.driver_trip

import com.roadlink.tripservice.domain.common.address.Address
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude
import java.util.UUID

data class DriverTripLegSolicitude(
    val tripLegSolicitudeId: UUID,
    val passenger: PassengerResult,
    val status: TripPlanSolicitude.TripLegSolicitude.Status,
    val addressJoinStart: Address,
    val addressJoinEnd: Address,
)
