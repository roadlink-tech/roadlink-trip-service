package com.roadlink.tripservice.infrastructure.rest.trip_solicitude.handler

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.roadlink.tripservice.domain.common.events.CommandBus
import com.roadlink.tripservice.domain.trip.TripRepository
import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitudeRepository
import com.roadlink.tripservice.domain.user.UserRepository
import com.roadlink.tripservice.domain.user.UserTrustScoreRepository
import com.roadlink.tripservice.infrastructure.End2EndTest
import com.roadlink.tripservice.usecases.trip.SectionFactory
import com.roadlink.tripservice.usecases.trip.TripFactory
import com.roadlink.tripservice.usecases.trip_solicitude.TripLegSolicitudeFactory
import com.roadlink.tripservice.usecases.trip_solicitude.plan.TripPlanSolicitudeFactory
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.uri.UriBuilder
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

@MicronautTest
class TripLegSolicitudesHandlerE2ETest : End2EndTest() {

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Inject
    private lateinit var objectMapper: ObjectMapper

    @Inject
    private lateinit var sectionRepository: SectionRepository

    @Inject
    private lateinit var tripRepository: TripRepository

    @Inject
    private lateinit var tripPlanSolicitudeRepository: TripPlanSolicitudeRepository

    @Inject
    private lateinit var userTrustScoreRepository: UserTrustScoreRepository

    @Inject
    private lateinit var userRepository: UserRepository

    @Inject
    private lateinit var commandBus: CommandBus

    @Test
    fun `accept two different trip leg solicitudes successfully`() {
        // given
        val callerId = UUID.randomUUID()
        val tripPlanSolicitudeId = UUID.randomUUID()
        val driverId = UUID.randomUUID()
        val tripLegSolicitudeId = UUID.randomUUID()
        val georgeId = UUID.randomUUID()

        val martinTripPlanSolicitudeId = UUID.randomUUID()
        val martinTripLegSolicitudeId = UUID.randomUUID()
        val martinId = UUID.randomUUID()

        val vehicleId = UUID.randomUUID()

        val trip = TripFactory.avCabildo4853_to_avCabildo20(driverId = driverId.toString())
        tripRepository.insert(trip)

        val section = SectionFactory.avCabildo(
            tripId = UUID.fromString(trip.id),
            initialAmountOfSeats = 3,
            driverId = driverId.toString(),
            vehicleId = vehicleId.toString()
        )
        sectionRepository.save(section)

        val georgeTripPlan = TripPlanSolicitudeFactory.common(
            id = tripPlanSolicitudeId,
            tripLegSolicitudes = listOf(
                TripLegSolicitudeFactory.common(
                    id = tripLegSolicitudeId,
                    passengerId = georgeId.toString(),
                    sections = listOf(section)
                )
            ),
        )

        val martinTripPlan = TripPlanSolicitudeFactory.common(
            id = martinTripPlanSolicitudeId,
            tripLegSolicitudes = listOf(
                TripLegSolicitudeFactory.common(
                    id = martinTripLegSolicitudeId,
                    passengerId = martinId.toString(),
                    sections = listOf(section)
                )
            ),
        )
        tripPlanSolicitudeRepository.insert(georgeTripPlan)
        tripPlanSolicitudeRepository.insert(martinTripPlan)

        entityManager.transaction.commit()

        val request =
            HttpRequest.PUT(
                UriBuilder.of(
                    "/trip-service/trip_leg_solicitudes/${
                        tripLegSolicitudeId
                    }/accept"
                ).build(), """"""
            ).header("X-Caller-Id", callerId.toString())

        // when
        val response = client.toBlocking().exchange(request, JsonNode::class.java)

        // THEN
        assertEquals(HttpStatus.ACCEPTED.code, response.code())

        val martinRequest =
            HttpRequest.PUT(
                UriBuilder.of(
                    "/trip-service/trip_leg_solicitudes/${
                        martinTripLegSolicitudeId
                    }/accept"
                ).build(), """"""
            ).header("X-Caller-Id", callerId.toString())

        // when
        val martinRequestResponse = client.toBlocking().exchange(martinRequest, JsonNode::class.java)

        // THEN
        assertEquals(HttpStatus.ACCEPTED.code, martinRequestResponse.code())
    }

}