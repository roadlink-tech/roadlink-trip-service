package com.roadlink.tripservice.infrastructure.rest.trip.handler

import com.fasterxml.jackson.databind.JsonNode
import com.roadlink.tripservice.domain.trip.feedback_solicitude.FeedbackSolicitudeRepository
import com.roadlink.tripservice.domain.trip_plan.TripPlan
import com.roadlink.tripservice.domain.trip_plan.TripPlan.*
import com.roadlink.tripservice.domain.trip_plan.TripPlan.Status.*
import com.roadlink.tripservice.domain.trip_plan.TripPlanRepository
import com.roadlink.tripservice.infrastructure.End2EndTest
import com.roadlink.tripservice.usecases.common.trip_point.TripPointFactory
import com.roadlink.tripservice.usecases.trip_plan.TripLegSectionFactory
import com.roadlink.tripservice.usecases.trip_plan.TripPlanFactory
import io.micronaut.context.annotation.Primary
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType.*
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.uri.UriBuilder
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.mockk
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONCompareMode
import java.util.*

@MicronautTest
internal class FinishTripHandlerE2ETest : End2EndTest() {

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Inject
    private lateinit var tripPlanRepository: TripPlanRepository

    @Singleton
    @Primary
    @Replaces(bean = FeedbackSolicitudeRepository::class)
    fun feedbackSolicitudeRepository(): FeedbackSolicitudeRepository = mockk(relaxed = true)

    @Test
    fun `when finish an existing trip, then it must work well`() {
        // given
        val oneTripLegId = UUID.randomUUID()
        val oneTripId = UUID.randomUUID()
        val twoTripLegId = UUID.randomUUID()
        val twoTripId = UUID.randomUUID()
        val tripPlan = givenATripPlanWithTwoTripLegs(oneTripLegId, oneTripId, twoTripLegId, twoTripId)
        tripPlanRepository.insert(tripPlan)
        entityManager.transaction.commit()

        val request = HttpRequest.POST(
            UriBuilder.of("/trip-service/trips/$oneTripId/finish").build(), ""
        )

        // when
        val response = client.toBlocking().exchange(request, JsonNode::class.java)

        // then
        assertEquals(HttpStatus.OK, response.status)
        assertEquals(APPLICATION_JSON_TYPE, response.contentType.get())
        JSONAssert.assertEquals(
            """
            [
               {
                  "id":"$oneTripLegId",
                  "tripId":"$oneTripId",
                  "status":"FINISHED"
               }
            ]
        """.trimIndent(), response.body().toString(), true
        )
    }

    @Test
    fun `when finish an existing trip and it belongs to more than one plan, then it must work well`() {
        // given
        val oneTripLegId = UUID.randomUUID()
        val anotherTripLegId = UUID.randomUUID()
        val tripId = UUID.randomUUID()
        val oneTripPlan = givenATripPlanWithASingleLeg(tripLegId = oneTripLegId, tripId = tripId)
        val otherTripPlan = givenATripPlanWithASingleLeg(tripLegId = anotherTripLegId, tripId = tripId)
        tripPlanRepository.insert(oneTripPlan)
        tripPlanRepository.insert(otherTripPlan)
        entityManager.transaction.commit()

        val request = HttpRequest.POST(
            UriBuilder.of("/trip-service/trips/$tripId/finish").build(), ""
        )

        // when
        val response = client.toBlocking().exchange(request, JsonNode::class.java)

        // then
        assertEquals(HttpStatus.OK, response.status)
        assertEquals(APPLICATION_JSON_TYPE, response.contentType.get())
        JSONAssert.assertEquals(
            """
            [
              {
                 "id":"$anotherTripLegId",
                 "tripId":"$tripId",
                 "status":"FINISHED"
              },
               {
                  "id":"$oneTripLegId",
                  "tripId":"$tripId",
                  "status":"FINISHED"
               }
            ]
        """.trimIndent(), response.body().toString(), JSONCompareMode.LENIENT
        )
    }

    private fun givenATripPlanWithASingleLeg(
        tripLegId: UUID = UUID.randomUUID(),
        tripId: UUID = UUID.randomUUID(),
    ): TripPlan {
        val oneTripLeg = TripLeg(
            id = tripLegId,
            tripId = tripId,
            driverId = UUID.randomUUID(),
            vehicleId = UUID.randomUUID(),
            sections = listOf(
                TripLegSectionFactory.common(
                    departure = TripPointFactory.avCabildo_20(),
                    arrival = TripPointFactory.avCabildo_1621()
                ),
                TripLegSectionFactory.common(
                    departure = TripPointFactory.avCabildo_1621(),
                    arrival = TripPointFactory.avDelLibertador_5000()
                ),
            ),
            status = NOT_FINISHED,
        )
        return TripPlanFactory.common(tripLegs = listOf(oneTripLeg))
    }

    private fun givenATripPlanWithTwoTripLegs(
        oneTripLegId: UUID = UUID.randomUUID(),
        oneTripId: UUID = UUID.randomUUID(),
        twoTripLegId: UUID = UUID.randomUUID(),
        twoTripId: UUID = UUID.randomUUID()
    ): TripPlan {
        val oneTripLeg = TripLeg(
            id = oneTripLegId,
            tripId = oneTripId,
            driverId = UUID.randomUUID(),
            vehicleId = UUID.randomUUID(),
            sections = listOf(
                TripLegSectionFactory.common(
                    departure = TripPointFactory.avCabildo_20(),
                    arrival = TripPointFactory.avCabildo_1621()
                ),
                TripLegSectionFactory.common(
                    departure = TripPointFactory.avCabildo_1621(),
                    arrival = TripPointFactory.avDelLibertador_5000()
                ),
            ),
            status = NOT_FINISHED,
        )
        val twoTripLeg = TripLeg(
            id = twoTripLegId,
            tripId = twoTripId,
            driverId = UUID.randomUUID(),
            vehicleId = UUID.randomUUID(),
            sections = listOf(
                TripLegSectionFactory.common(
                    departure = TripPointFactory.avDelLibertador_5000(),
                    arrival = TripPointFactory.avCabildo_4853()
                ),
                TripLegSectionFactory.common(
                    departure = TripPointFactory.avCabildo_4853(),
                    arrival = TripPointFactory.rosario()
                ),
            ),
            status = NOT_FINISHED,
        )
        return TripPlanFactory.common(tripLegs = listOf(oneTripLeg, twoTripLeg))
    }
}