package com.roadlink.tripservice.infrastructure.rest.trip_application.mapper

import com.roadlink.tripservice.domain.driver_trip.DriverTripApplication
import com.roadlink.tripservice.domain.trip_application.TripPlanApplication
import com.roadlink.tripservice.infrastructure.rest.driver_trip.response.PassengerResultResponseMapper
import com.roadlink.tripservice.infrastructure.rest.common.address.AddressResponseMapper
import com.roadlink.tripservice.infrastructure.rest.trip_application.response.DriverTripApplicationResponse
import com.roadlink.tripservice.infrastructure.rest.trip_application.response.TripApplicationStatusResponse

object DriverTripApplicationResponseMapper {
    fun map(driverTripApplication: DriverTripApplication): DriverTripApplicationResponse =
        DriverTripApplicationResponse(
            tripApplicationId = driverTripApplication.tripApplicationId.toString(),
            passenger = PassengerResultResponseMapper.map(driverTripApplication.passenger),
            applicationStatus = applicationStatus(driverTripApplication.applicationStatus),
            addressJoinStart = AddressResponseMapper.map(driverTripApplication.addressJoinStart),
            addressJoinEnd = AddressResponseMapper.map(driverTripApplication.addressJoinEnd),
        )

    private fun applicationStatus(applicationStatus: TripPlanApplication.TripApplication.Status): TripApplicationStatusResponse =
        when (applicationStatus) {
            TripPlanApplication.TripApplication.Status.PENDING_APPROVAL ->
                TripApplicationStatusResponse.PENDING_APPROVAL
            TripPlanApplication.TripApplication.Status.REJECTED ->
                TripApplicationStatusResponse.REJECTED
            TripPlanApplication.TripApplication.Status.CONFIRMED ->
                TripApplicationStatusResponse.CONFIRMED
        }

}