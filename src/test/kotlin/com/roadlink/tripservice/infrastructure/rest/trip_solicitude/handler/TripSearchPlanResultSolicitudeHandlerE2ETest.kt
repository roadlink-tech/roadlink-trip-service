package com.roadlink.tripservice.infrastructure.rest.trip_solicitude.handler

import com.fasterxml.jackson.databind.JsonNode
import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude.*
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude.TripLegSolicitude.*
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitudeRepository
import com.roadlink.tripservice.infrastructure.End2EndTest
import com.roadlink.tripservice.usecases.factory.SectionFactory
import com.roadlink.tripservice.usecases.trip.TripFactory
import com.roadlink.tripservice.usecases.trip_solicitude.plan.TripPlanSolicitudeFactory
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.uri.UriBuilder
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.JSONAssert
import java.util.*

@MicronautTest
class TripSearchPlanResultSolicitudeHandlerE2ETest : End2EndTest() {

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Inject
    private lateinit var tripPlanSolicitudeRepository: TripPlanSolicitudeRepository

    @Inject
    private lateinit var entityManager: EntityManager

    @Inject
    private lateinit var sectionRepository: SectionRepository

    /**
     * Retrieve Trip Plan Application
     */
    @Test
    fun `retrieve an existing trip plan`() {
        // GIVEN
        val tripPlanSolicitudeId = UUID.randomUUID()
        val tripId = UUID.fromString(TripFactory.avCabildo_id)
        val tripLegSolicitudeId = UUID.randomUUID()
        val passengerId: UUID = UUID.randomUUID()
        givenASavedSection(tripId)
        givenASavedTripPlanSolicitude(
            tripPlanSolicitudeId = tripPlanSolicitudeId,
            passengerId = passengerId,
            tripApplicationId = tripLegSolicitudeId,
            tripLegSolicitudeStatus = TripLegSolicitude.Status.PENDING_APPROVAL
        )
        entityManager.transaction.commit()
        val request: HttpRequest<Any> = HttpRequest.GET(
            UriBuilder.of("/trip-service/trip_plan_solicitudes/$tripPlanSolicitudeId").build(),
        )

        // WHEN
        val response = client.toBlocking().exchange(request, JsonNode::class.java)

        // THEN
        JSONAssert.assertEquals(
            """
            {
              "id": "$tripPlanSolicitudeId",
              "tripLegSolicitudes": [
                {
                  "id": "$tripLegSolicitudeId",
                  "passengerId": "$passengerId",
                  "sections": [
                    {
                      "id": "SectionFactory-avCabildo",
                      "tripId": "$tripId",
                      "departure": {
                        "estimatedArrivalTime": 1665835200,
                        "address": {
                          "location": {
                            "latitude": -34.540412,
                            "longitude": -58.474732
                          },
                          "fullAddress": "Av. Cabildo 4853, Buenos Aires",
                          "street": "Av. Cabildo",
                          "city": "Buenos Aires",
                          "country": "Argentina",
                          "houseNumber": "4853"
                        }
                      },
                      "arrival": {
                        "estimatedArrivalTime": 1665856800,
                        "address": {
                          "location": {
                            "latitude": -34.574810,
                            "longitude": -58.435990
                          },
                          "fullAddress": "Av. Cabildo 20, Buenos Aires",
                          "street": "Av. Cabildo",
                          "city": "Buenos Aires",
                          "country": "Argentina",
                          "houseNumber": "20"
                        }
                      },
                      "distanceInMeters": 6070,
                      "driverId": "John Smith",
                      "vehicleId": "Ford mustang",
                      "initialAmountOfSeats": 4,
                      "bookedSeats": 0
                    }
                  ],
                  "status": "PENDING_APPROVAL",
                  "authorizerId": "authorizerId"
                }
              ],
              "status": "PENDING_APPROVAL"
            }
        """.trimIndent(), response.body().toString(), true
        )
    }

    private fun givenASavedSection(tripId: UUID) {
        sectionRepository.save(SectionFactory.avCabildo(tripId = tripId))
    }

    private fun givenASavedTripPlanSolicitude(
        tripPlanSolicitudeId: UUID,
        tripApplicationId: UUID,
        passengerId: UUID = UUID.randomUUID(),
        tripLegSolicitudeStatus: TripLegSolicitude.Status = TripLegSolicitude.Status.PENDING_APPROVAL
    ) {
        tripPlanSolicitudeRepository.insert(
            TripPlanSolicitudeFactory.withASingleTripApplication(
                id = tripPlanSolicitudeId,
                tripApplicationId = tripApplicationId,
                passengerId = passengerId.toString(),
                tripApplicationStatus = tripLegSolicitudeStatus,
            )
        )
    }

    /**
     * List Trip Plan Application
     */
    @Test
    fun `listing all the trip plan solicitudes by passenger id`() {
        // GIVEN
        val tripPlanSolicitudeId = UUID.randomUUID()
        val tripId = UUID.fromString(TripFactory.avCabildo_id)
        val passengerId = UUID.randomUUID()
        val tripApplicationId = UUID.randomUUID()
        givenASavedSection(tripId)
        givenASavedTripPlanSolicitude(tripPlanSolicitudeId, tripApplicationId, passengerId)
        entityManager.transaction.commit()

        val request: HttpRequest<Any> = HttpRequest.GET(
            UriBuilder.of("/trip-service/users/$passengerId/trip_plan_solicitudes").build(),
        )

        // WHEN
        val response = client.toBlocking().exchange(request, JsonNode::class.java)

        // THEN
        JSONAssert.assertEquals(
            """
            [
               {
                  "id":"$tripPlanSolicitudeId",
                  "tripLegSolicitudes":[
                     {
                        "id":"$tripApplicationId",
                        "passengerId":"$passengerId",
                        "sections":[
                           {
                              "id":"SectionFactory-avCabildo",
                              "tripId":"$tripId",
                              "departure":{
                                 "estimatedArrivalTime":1.6658352E9,
                                 "address":{
                                    "location": {
                                      "latitude": -34.540412,
                                      "longitude": -58.474732
                                    },
                                    "fullAddress":"Av. Cabildo 4853, Buenos Aires",
                                    "street":"Av. Cabildo",
                                    "city":"Buenos Aires",
                                    "country":"Argentina",
                                    "houseNumber":"4853"
                                 }
                              },
                              "arrival":{
                                 "estimatedArrivalTime":1.6658568E9,
                                 "address":{
                                    "location": {
                                      "latitude": -34.574810,
                                      "longitude": -58.435990
                                    },
                                    "fullAddress":"Av. Cabildo 20, Buenos Aires",
                                    "street":"Av. Cabildo",
                                    "city":"Buenos Aires",
                                    "country":"Argentina",
                                    "houseNumber":"20"
                                 }
                              },
                              "distanceInMeters":6070.0,
                              "driverId":"John Smith",
                              "vehicleId":"Ford mustang",
                              "initialAmountOfSeats":4,
                              "bookedSeats":0
                           }
                        ],
                        "status":"PENDING_APPROVAL",
                        "authorizerId":"authorizerId"
                     }
                  ],
                  "status":"PENDING_APPROVAL"
               }
            ]
        """.trimIndent(), response.body().toString(), true
        )
    }

    /**
     * List Trip Plan Application
     */
    @Test
    fun `listing all the trip plan applications by passenger id and status`() {
        // GIVEN
        val tripId = UUID.fromString(TripFactory.avCabildo_id)
        givenASavedSection(tripId)

        val tripPlanSolicitudeId = UUID.randomUUID()
        val passengerId = UUID.randomUUID()
        val tripApplicationId = UUID.randomUUID()
        givenASavedTripPlanSolicitude(
            tripPlanSolicitudeId,
            tripApplicationId,
            passengerId,
            TripLegSolicitude.Status.REJECTED
        )

        val anotherTripPlanSolicitudeId = UUID.randomUUID()
        val anotherPassengerId = UUID.randomUUID()
        val anotherTripApplicationId = UUID.randomUUID()

        givenASavedTripPlanSolicitude(
            anotherTripPlanSolicitudeId,
            anotherPassengerId,
            anotherTripApplicationId,
            TripLegSolicitude.Status.PENDING_APPROVAL
        )

        entityManager.transaction.commit()

        val request: HttpRequest<Any> = HttpRequest.GET(
            UriBuilder.of("/trip-service/users/$passengerId/trip_plan_solicitudes?status=REJECTED")
                .build(),
        )

        // WHEN
        val response = client.toBlocking().exchange(request, JsonNode::class.java)

        // THEN
        JSONAssert.assertEquals(
            """
            [
               {
                  "id":"$tripPlanSolicitudeId",
                  "tripLegSolicitudes":[
                     {
                        "id":"$tripApplicationId",
                        "passengerId":"$passengerId",
                        "sections":[
                           {
                              "id":"SectionFactory-avCabildo",
                              "tripId":"$tripId",
                              "departure":{
                                 "estimatedArrivalTime":1.6658352E9,
                                 "address":{
                                    "location": {
                                      "latitude": -34.540412,
                                      "longitude": -58.474732
                                    },
                                    "fullAddress":"Av. Cabildo 4853, Buenos Aires",
                                    "street":"Av. Cabildo",
                                    "city":"Buenos Aires",
                                    "country":"Argentina",
                                    "houseNumber":"4853"
                                 }
                              },
                              "arrival":{
                                 "estimatedArrivalTime":1.6658568E9,
                                 "address":{
                                    "location": {
                                      "latitude": -34.574810,
                                      "longitude": -58.435990
                                    },
                                    "fullAddress":"Av. Cabildo 20, Buenos Aires",
                                    "street":"Av. Cabildo",
                                    "city":"Buenos Aires",
                                    "country":"Argentina",
                                    "houseNumber":"20"
                                 }
                              },
                              "distanceInMeters":6070.0,
                              "driverId":"John Smith",
                              "vehicleId":"Ford mustang",
                              "initialAmountOfSeats":4,
                              "bookedSeats":0
                           }
                        ],
                        "status":"REJECTED",
                        "authorizerId":"authorizerId"
                     }
                  ],
                  "status":"REJECTED"
               }
            ]
        """.trimIndent(), response.body().toString(), true
        )
    }


}