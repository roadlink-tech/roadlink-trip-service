package com.roadlink.tripservice.infrastructure.rest.mappers

import com.roadlink.tripservice.domain.*
import com.roadlink.tripservice.infrastructure.rest.responses.*

object DriverTripDetailResponseMapper {
    fun map(driverTripDetail: DriverTripDetail): DriverTripDetailResponse =
        DriverTripDetailResponse(
            tripId = driverTripDetail.tripId,
            tripStatus = tripStatusResponse(driverTripDetail.tripStatus),
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

    private fun tripStatusResponse(tripStatus: TripStatus): TripStatusResponse =
        when (tripStatus) {
            TripStatus.NOT_STARTED -> TripStatusResponse.NOT_STARTED
            TripStatus.IN_PROGRESS -> TripStatusResponse.IN_PROGRESS
            TripStatus.FINISHED -> TripStatusResponse.FINISHED
        }

    private fun seatsAvailabilityStatusResponse(seatsAvailabilityStatus: SeatsAvailabilityStatus): SeatsAvailabilityStatusResponse =
        when (seatsAvailabilityStatus) {
            SeatsAvailabilityStatus.ALL_SEATS_AVAILABLE -> SeatsAvailabilityStatusResponse.ALL_SEATS_AVAILABLE
            SeatsAvailabilityStatus.SOME_SEATS_AVAILABLE -> SeatsAvailabilityStatusResponse.SOME_SEATS_AVAILABLE
            SeatsAvailabilityStatus.NO_SEATS_AVAILABLE -> SeatsAvailabilityStatusResponse.NO_SEATS_AVAILABLE
        }
}
