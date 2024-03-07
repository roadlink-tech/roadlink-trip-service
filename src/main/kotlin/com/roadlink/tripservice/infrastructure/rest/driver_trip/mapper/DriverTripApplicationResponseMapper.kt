package com.roadlink.tripservice.infrastructure.rest.driver_trip.mapper

import com.roadlink.tripservice.domain.driver_trip.DriverTripApplication
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude
import com.roadlink.tripservice.infrastructure.rest.driver_trip.response.PassengerResultResponseMapper
import com.roadlink.tripservice.infrastructure.rest.common.address.AddressResponseMapper
import com.roadlink.tripservice.infrastructure.rest.driver_trip.response.DriverTripApplicationResponse
import com.roadlink.tripservice.infrastructure.rest.trip_solicitude.response.TripApplicationStatusResponse

object DriverTripApplicationResponseMapper {
    fun map(driverTripApplication: DriverTripApplication): DriverTripApplicationResponse =
        DriverTripApplicationResponse(
            tripApplicationId = driverTripApplication.tripApplicationId.toString(),
            passenger = PassengerResultResponseMapper.map(driverTripApplication.passenger),
            applicationStatus = applicationStatus(driverTripApplication.applicationStatus),
            addressJoinStart = AddressResponseMapper.map(driverTripApplication.addressJoinStart),
            addressJoinEnd = AddressResponseMapper.map(driverTripApplication.addressJoinEnd),
        )

    private fun applicationStatus(applicationStatus: TripPlanSolicitude.TripLegSolicitude.Status): TripApplicationStatusResponse =
        when (applicationStatus) {
            TripPlanSolicitude.TripLegSolicitude.Status.PENDING_APPROVAL ->
                TripApplicationStatusResponse.PENDING_APPROVAL
            TripPlanSolicitude.TripLegSolicitude.Status.REJECTED ->
                TripApplicationStatusResponse.REJECTED
            TripPlanSolicitude.TripLegSolicitude.Status.CONFIRMED ->
                TripApplicationStatusResponse.CONFIRMED
        }

}