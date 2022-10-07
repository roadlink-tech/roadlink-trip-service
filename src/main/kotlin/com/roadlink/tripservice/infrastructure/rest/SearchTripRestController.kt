package com.roadlink.tripservice.infrastructure.rest

import com.fasterxml.jackson.annotation.JsonInclude
import com.roadlink.tripservice.domain.Location
import com.roadlink.tripservice.domain.TripPlan
import com.roadlink.tripservice.domain.TripPoint
import com.roadlink.tripservice.usecases.SearchTrip
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue
import java.time.Instant

@Controller("/trips")
class SearchTripRestController(private val searchTrip: SearchTrip) {
    @Get
    fun handle(
        @QueryValue departureLatitude: Double,
        @QueryValue departureLongitude: Double,
        @QueryValue arrivalLatitude: Double,
        @QueryValue arrivalLongitude: Double,
        @QueryValue at: String,
    ): SearchTripResponse {

        val tripPlans = searchTrip(SearchTrip.Request(
            departure = Location(
                latitude = departureLatitude, longitude = departureLongitude,
            ),
            arrival = Location(
                latitude = arrivalLatitude, longitude = arrivalLongitude,
            ),
            at = Instant.parse(at),
        ))

        return toSearchTripResponse(tripPlans)
    }

    private fun toSearchTripResponse(tripPlans: List<TripPlan>): SearchTripResponse {
        return SearchTripResponse(tripPlans = tripPlans.map { tripPlan ->
            TripPlanResponse(sections = tripPlan.sections.map { section ->
                SectionResponse(
                    departure = toTripPointResponse(section.departure),
                    arrival = toTripPointResponse(section.arrival),
                )
            })
        })
    }

    private fun toTripPointResponse(tripPoint: TripPoint): TripPointResponse {
        return TripPointResponse(
            location = LocationResponse(
                latitude = tripPoint.location.latitude,
                longitude = tripPoint.location.longitude,
            ),
            at = tripPoint.at.toString(),
        )
    }
}

@JsonInclude(JsonInclude.Include.ALWAYS)
data class SearchTripResponse(val tripPlans: List<TripPlanResponse>)

data class TripPlanResponse(val sections: List<SectionResponse>)

data class SectionResponse(val departure: TripPointResponse, val arrival: TripPointResponse)

data class TripPointResponse(val location: LocationResponse, val at: String)

data class LocationResponse(val latitude: Double, val longitude: Double)
