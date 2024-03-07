package com.roadlink.tripservice.infrastructure.rest.trip_solicitude.response

import com.roadlink.tripservice.domain.common.address.Address
import com.roadlink.tripservice.domain.common.TripPoint
import com.roadlink.tripservice.domain.trip.section.Section
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude
import com.roadlink.tripservice.infrastructure.rest.ApiResponse
import java.time.Instant
import java.util.*

data class TripPlanSolicitudeResponse(
    val id: UUID,
    val tripApplications: List<TripLegSolicitudeResponse>,
    val status: String
) : ApiResponse {
    data class TripLegSolicitudeResponse(
        val id: UUID,
        val passengerId: String,
        val sections: List<SectionResponse>,
        val status: String,
        val authorizerId: String
    )

    data class AddressResponse(
        val fullAddress: String,
        val street: String,
        val city: String,
        val country: String,
        val houseNumber: String,
    ) {

        companion object {
            fun from(address: Address): AddressResponse {
                return AddressResponse(
                    fullAddress = address.fullAddress,
                    street = address.street,
                    city = address.city,
                    country = address.country,
                    houseNumber = address.houseNumber
                )
            }
        }
    }

    data class TripPointResponse(
        val estimatedArrivalTime: Instant,
        val address: AddressResponse,
    ) {
        companion object {
            fun from(point: TripPoint): TripPointResponse {
                return TripPointResponse(
                    estimatedArrivalTime = point.estimatedArrivalTime,
                    address = AddressResponse.from(point.address)
                )
            }
        }
    }

    data class SectionResponse(
        val id: String,
        val tripId: UUID,
        val departure: TripPointResponse,
        val arrival: TripPointResponse,
        val distanceInMeters: Double,
        val driverId: String,
        val vehicleId: String,
        var initialAmountOfSeats: Int,
        var bookedSeats: Int
    ) {

        companion object {
            fun from(sections: List<Section>): List<SectionResponse> {
                return sections.map {
                    SectionResponse(
                        id = it.id,
                        tripId = it.tripId,
                        departure = TripPointResponse.from(it.departure),
                        arrival = TripPointResponse.from(it.arrival),
                        distanceInMeters = it.distanceInMeters,
                        driverId = it.driverId,
                        vehicleId = it.vehicleId,
                        initialAmountOfSeats = it.initialAmountOfSeats,
                        bookedSeats = it.bookedSeats
                    )
                }
            }
        }
    }

    companion object {
        fun from(output: TripPlanSolicitude): TripPlanSolicitudeResponse {
            return TripPlanSolicitudeResponse(
                id = output.id,
                tripApplications = output.tripLegSolicitudes.map {
                    TripLegSolicitudeResponse(
                        id = it.id,
                        passengerId = it.passengerId,
                        status = it.status.toString(),
                        sections = SectionResponse.from(it.sections),
                        authorizerId = it.authorizerId
                    )
                },
                status = output.status().toString()
            )
        }
    }
}