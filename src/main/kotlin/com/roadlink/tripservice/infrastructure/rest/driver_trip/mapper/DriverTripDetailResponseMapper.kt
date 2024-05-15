package com.roadlink.tripservice.infrastructure.rest.driver_trip.mapper

import com.roadlink.tripservice.domain.driver_trip.DriverTripDetail
import com.roadlink.tripservice.domain.driver_trip.SeatsAvailabilityStatus
import com.roadlink.tripservice.domain.trip.TripStatus
import com.roadlink.tripservice.infrastructure.rest.common.trip_point.TripPointResponseMapper
import com.roadlink.tripservice.infrastructure.rest.driver_trip.response.*

object DriverTripDetailResponseMapper {
    fun map(driverTripDetail: DriverTripDetail): DriverTripDetailResponse =
        DriverTripDetailResponse(
            tripId = driverTripDetail.tripId,
            tripStatus = DriverTripStatusResponse.from(driverTripDetail.tripStatus),
            seatStatus = seatsAvailabilityStatusResponse(driverTripDetail.seatStatus),
            hasPendingApplications = driverTripDetail.hasPendingApplications,
            sectionDetails = driverTripDetail.sectionDetails.map { driverSectionDetail ->
                DriverSectionDetailResponse(
                    sectionId = driverSectionDetail.sectionId,
                    departure = TripPointResponseMapper.map(driverSectionDetail.departure),
                    arrival = TripPointResponseMapper.map(driverSectionDetail.arrival),
                    occupiedSeats = driverSectionDetail.occupiedSeats,
                    availableSeats = driverSectionDetail.availableSeats,
                    passengers = driverSectionDetail.passengers.map { passenger ->
                        PassengerResultResponseMapper.map(passenger)
                    }
                )
            }
        )

    private fun tripStatusResponse(tripStatus: TripStatus): DriverTripStatusResponse =
        when (tripStatus) {
            TripStatus.NOT_STARTED -> DriverTripStatusResponse.NOT_STARTED
            TripStatus.IN_PROGRESS -> DriverTripStatusResponse.IN_PROGRESS
            TripStatus.FINISHED -> DriverTripStatusResponse.FINISHED
        }

    private fun seatsAvailabilityStatusResponse(seatsAvailabilityStatus: SeatsAvailabilityStatus): SeatsAvailabilityStatusResponse =
        when (seatsAvailabilityStatus) {
            SeatsAvailabilityStatus.ALL_SEATS_AVAILABLE -> SeatsAvailabilityStatusResponse.ALL_SEATS_AVAILABLE
            SeatsAvailabilityStatus.SOME_SEATS_AVAILABLE -> SeatsAvailabilityStatusResponse.SOME_SEATS_AVAILABLE
            SeatsAvailabilityStatus.NO_SEATS_AVAILABLE -> SeatsAvailabilityStatusResponse.NO_SEATS_AVAILABLE
        }
}
