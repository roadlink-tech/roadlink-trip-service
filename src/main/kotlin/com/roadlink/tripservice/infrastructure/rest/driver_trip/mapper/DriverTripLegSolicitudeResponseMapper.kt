package com.roadlink.tripservice.infrastructure.rest.driver_trip.mapper

import com.roadlink.tripservice.domain.driver_trip.DriverTripLegSolicitude
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude
import com.roadlink.tripservice.infrastructure.rest.driver_trip.response.PassengerResultResponseMapper
import com.roadlink.tripservice.infrastructure.rest.common.address.AddressResponseMapper
import com.roadlink.tripservice.infrastructure.rest.driver_trip.response.DriverTripLegSolicitudeResponse
import com.roadlink.tripservice.infrastructure.rest.trip_solicitude.response.TripLegSolicitudeStatusResponse

object DriverTripLegSolicitudeResponseMapper {
    fun map(driverTripLegSolicitude: DriverTripLegSolicitude): DriverTripLegSolicitudeResponse =
        DriverTripLegSolicitudeResponse(
            tripLegSolicitudeId = driverTripLegSolicitude.tripLegSolicitudeId.toString(),
            passenger = PassengerResultResponseMapper.map(driverTripLegSolicitude.passenger),
            status = status(driverTripLegSolicitude.status),
            addressJoinStart = AddressResponseMapper.map(driverTripLegSolicitude.addressJoinStart),
            addressJoinEnd = AddressResponseMapper.map(driverTripLegSolicitude.addressJoinEnd),
        )

    private fun status(tripLegSolicitudeStatus: TripPlanSolicitude.TripLegSolicitude.Status): TripLegSolicitudeStatusResponse =
        when (tripLegSolicitudeStatus) {
            TripPlanSolicitude.TripLegSolicitude.Status.PENDING_APPROVAL ->
                TripLegSolicitudeStatusResponse.PENDING_APPROVAL
            TripPlanSolicitude.TripLegSolicitude.Status.REJECTED ->
                TripLegSolicitudeStatusResponse.REJECTED
            TripPlanSolicitude.TripLegSolicitude.Status.CONFIRMED ->
                TripLegSolicitudeStatusResponse.CONFIRMED
        }

}