package com.roadlink.tripservice.infrastructure.rest.trip_plan

import com.fasterxml.jackson.databind.JsonNode
import com.roadlink.tripservice.domain.trip_plan.TripPlan
import com.roadlink.tripservice.domain.trip_plan.TripPlan.*
import com.roadlink.tripservice.domain.trip_plan.TripPlan.Status.*
import com.roadlink.tripservice.domain.trip_plan.TripPlanRepository
import com.roadlink.tripservice.infrastructure.End2EndTest
import com.roadlink.tripservice.usecases.trip_plan.TripPlanFactory
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.uri.UriBuilder
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.JSONAssert
import java.util.*

@MicronautTest
class TripPlanRestHandlerE2ETest : End2EndTest() {

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Inject
    private lateinit var tripPlanRepository: TripPlanRepository

    /**
     * List Trip
     */
    @Test
    fun `list trip plans by passenger id`() {
        // given
        val userId = UUID.randomUUID()
        val savedTripPlan = givenASavedTripPlanWithPassengerId(userId)
        val request: HttpRequest<Any> = givenARequest(userId)

        entityManager.transaction.commit()

        // when
        val response = client.toBlocking().exchange(request, JsonNode::class.java)

        // then
        assertEquals(HttpStatus.OK, response.status)
        assertEquals(MediaType.APPLICATION_JSON_TYPE, response.contentType.get())
        JSONAssert.assertEquals(
            """
            [
               {
                  "id":"${savedTripPlan.id}",
                  "trip_legs":[
                     {
                        "id":"${savedTripPlan.tripLegs.first().id}",
                        "trip_id":"${savedTripPlan.tripLegs.first().tripId}",
                        "driver_id":"${savedTripPlan.tripLegs.first().driverId}",
                        "vehicle_id":"${savedTripPlan.tripLegs.first().vehicleId}"
                     }
                  ],
                  "passenger_id":"$userId",
                  "status":"NOT_FINISHED"
               }
            ]
        """.trimIndent(), response.body().toString(), true
        )
    }

    @Test
    fun `list trip plans with more than a single result`() {
        // given
        val userId = UUID.randomUUID()
        val savedTripPlan = TripPlanFactory.common(passengerId = userId, status = NOT_FINISHED)
        val otherTripPlan = TripPlanFactory.common(passengerId = userId, status = FINISHED)
        tripPlanRepository.insert(savedTripPlan)
        tripPlanRepository.insert(otherTripPlan)
        entityManager.transaction.commit()

        val request: HttpRequest<Any> = givenARequest(userId)

        // when
        val response = client.toBlocking().exchange(request, JsonNode::class.java)

        // then
        assertEquals(HttpStatus.OK, response.status)
        assertEquals(MediaType.APPLICATION_JSON_TYPE, response.contentType.get())
        JSONAssert.assertEquals(
            """
            [
               {
                  "id":"${savedTripPlan.id}",
                  "trip_legs":[
                     {
                        "id":"${savedTripPlan.tripLegs.first().id}",
                        "trip_id":"${savedTripPlan.tripLegs.first().tripId}",
                        "driver_id":"${savedTripPlan.tripLegs.first().driverId}",
                        "vehicle_id":"${savedTripPlan.tripLegs.first().vehicleId}"
                     }
                  ],
                  "passenger_id":"$userId",
                  "status":"NOT_FINISHED"
               },
               {
                  "id":"${otherTripPlan.id}",
                  "trip_legs":[
                     {
                        "id":"${otherTripPlan.tripLegs.first().id}",
                        "trip_id":"${otherTripPlan.tripLegs.first().tripId}",
                        "driver_id":"${otherTripPlan.tripLegs.first().driverId}",
                        "vehicle_id":"${otherTripPlan.tripLegs.first().vehicleId}"
                     }
                  ],
                  "passenger_id":"$userId",
                  "status":"FINISHED"
               }
            ]
        """.trimIndent(), response.body().toString(), true
        )
    }

    @Test
    fun `list trip plans by passenger id and status`() {
        // given
        val userId = UUID.randomUUID()
        val savedTripPlan = TripPlanFactory.common(passengerId = userId, status = FINISHED)
        val otherTripPlan = TripPlanFactory.common(passengerId = userId, status = NOT_FINISHED)
        tripPlanRepository.insert(savedTripPlan)
        tripPlanRepository.insert(otherTripPlan)
        entityManager.transaction.commit()

        val request: HttpRequest<Any> = HttpRequest
            .GET(
                UriBuilder.of("/trip-service/users/$userId/trip_plans?status=NOT_FINISHED")
                    .build()
            )


        // when
        val response = client.toBlocking().exchange(request, JsonNode::class.java)

        // then
        assertEquals(HttpStatus.OK, response.status)
        assertEquals(MediaType.APPLICATION_JSON_TYPE, response.contentType.get())
        JSONAssert.assertEquals(
            """
            [
               {
                  "id":"${otherTripPlan.id}",
                  "trip_legs":[
                     {
                        "id":"${otherTripPlan.tripLegs.first().id}",
                        "trip_id":"${otherTripPlan.tripLegs.first().tripId}",
                        "driver_id":"${otherTripPlan.tripLegs.first().driverId}",
                        "vehicle_id":"${otherTripPlan.tripLegs.first().vehicleId}"
                     }
                  ],
                  "passenger_id":"$userId",
                  "status":"NOT_FINISHED"
               }
            ]
        """.trimIndent(), response.body().toString(), true
        )
    }

    @Test
    fun `list trip plans by passenger id and status, but without any result`() {
        // given
        val userId = UUID.randomUUID()
        val savedTripPlan = TripPlanFactory.common(passengerId = userId, status = FINISHED)
        val otherTripPlan = TripPlanFactory.common(passengerId = userId, status = NOT_FINISHED)
        tripPlanRepository.insert(savedTripPlan)
        tripPlanRepository.insert(otherTripPlan)
        entityManager.transaction.commit()

        val request: HttpRequest<Any> = HttpRequest
            .GET(
                UriBuilder.of("/trip-service/users/$userId/trip_plans?status=CANCELLED")
                    .build()
            )


        // when
        val response = client.toBlocking().exchange(request, JsonNode::class.java)

        // then
        assertEquals(HttpStatus.OK, response.status)
        assertEquals(MediaType.APPLICATION_JSON_TYPE, response.contentType.get())
        JSONAssert.assertEquals("""[]""".trimIndent(), response.body().toString(), true)
    }

    @Test
    fun `list trip plans with by passenger id and different status`() {
        // given
        val userId = UUID.randomUUID()
        val savedTripPlan = TripPlanFactory.common(passengerId = userId, status = NOT_FINISHED)
        val otherTripPlan = TripPlanFactory.common(passengerId = userId, status = FINISHED)
        tripPlanRepository.insert(savedTripPlan)
        tripPlanRepository.insert(otherTripPlan)
        entityManager.transaction.commit()

        val request: HttpRequest<Any> = HttpRequest
            .GET(
                UriBuilder.of("/trip-service/users/$userId/trip_plans?status=NOT_FINISHED,FINISHED,CANCELLED")
                    .build()
            )

        // when
        val response = client.toBlocking().exchange(request, JsonNode::class.java)

        // then
        assertEquals(HttpStatus.OK, response.status)
        assertEquals(MediaType.APPLICATION_JSON_TYPE, response.contentType.get())
        JSONAssert.assertEquals(
            """
            [
               {
                  "id":"${savedTripPlan.id}",
                  "trip_legs":[
                     {
                        "id":"${savedTripPlan.tripLegs.first().id}",
                        "trip_id":"${savedTripPlan.tripLegs.first().tripId}",
                        "driver_id":"${savedTripPlan.tripLegs.first().driverId}",
                        "vehicle_id":"${savedTripPlan.tripLegs.first().vehicleId}"
                     }
                  ],
                  "passenger_id":"$userId",
                  "status":"NOT_FINISHED"
               },
               {
                  "id":"${otherTripPlan.id}",
                  "trip_legs":[
                     {
                        "id":"${otherTripPlan.tripLegs.first().id}",
                        "trip_id":"${otherTripPlan.tripLegs.first().tripId}",
                        "driver_id":"${otherTripPlan.tripLegs.first().driverId}",
                        "vehicle_id":"${otherTripPlan.tripLegs.first().vehicleId}"
                     }
                  ],
                  "passenger_id":"$userId",
                  "status":"FINISHED"
               }
            ]
        """.trimIndent(), response.body().toString(), true
        )
    }

    private fun givenARequest(userId: UUID?): HttpRequest<Any> =
        HttpRequest
            .GET(
                UriBuilder.of("/trip-service/users/$userId/trip_plans")
                    .build()
            )

    private fun givenASavedTripPlanWithPassengerId(userId: UUID): TripPlan {
        tripPlanRepository.insert(TripPlanFactory.common(passengerId = userId)).also { return it }
    }

}