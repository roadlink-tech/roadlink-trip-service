package com.roadlink.tripservice.domain.driver_trip

import com.roadlink.tripservice.domain.common.address.Address
import com.roadlink.tripservice.domain.trip_application.TripPlanApplication
import java.util.UUID

data class DriverTripApplication(
    val tripApplicationId: UUID,
    val passenger: PassengerResult,
    val applicationStatus: TripPlanApplication.TripApplication.Status,
    val addressJoinStart: Address,
    val addressJoinEnd: Address,
)
