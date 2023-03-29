package com.roadlink.tripservice.dev_tools.infrastructure.rest

import com.roadlink.tripservice.dev_tools.domain.GeoapifyPointNotExists
import com.roadlink.tripservice.dev_tools.usecases.AddSection
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import java.time.Instant

@Controller("/trip-service/dev-tools/add-section")
class AddSectionRestController(private val addSection: AddSection) {
    @Post
    fun handle(@Body body: HttpRequestBody): HttpResponse<String> {
        return try {
            val request = body.toRequest()
            addSection(request)
            HttpResponse.ok()
        } catch (e: GeoapifyPointNotExists) {
            HttpResponse.badRequest(e.message ?: "")
        }
    }

    private fun HttpRequestBody.toRequest(): AddSection.Request =
        AddSection.Request(
            departure = AddSection.Request.TripPoint(
                name = departure.name,
                at = Instant.parse(departure.at),
            ),
            arrival = AddSection.Request.TripPoint(
                name = arrival.name,
                at = Instant.parse(arrival.at),
            ),
            distanceInMeters = distanceInMeters,
            driver = driver,
            vehicle = vehicle,
            availableSeats = availableSeats,
        )

    data class HttpRequestBody(
        val departure: TripPoint,
        val arrival: TripPoint,
        val distanceInMeters: Double,
        val driver: String,
        val vehicle: String,
        val availableSeats: Int,
    ) {
        data class TripPoint(
            val name: String,
            val at: String,
        )
    }
}