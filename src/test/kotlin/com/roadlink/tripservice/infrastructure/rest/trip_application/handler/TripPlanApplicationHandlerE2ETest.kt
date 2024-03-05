package com.roadlink.tripservice.infrastructure.rest.trip_application.handler

import com.fasterxml.jackson.databind.JsonNode
import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.domain.trip_application.TripPlanApplication.*
import com.roadlink.tripservice.domain.trip_application.TripPlanApplication.TripApplication.*
import com.roadlink.tripservice.domain.trip_application.TripPlanApplicationRepository
import com.roadlink.tripservice.infrastructure.End2EndTest
import com.roadlink.tripservice.usecases.factory.SectionFactory
import com.roadlink.tripservice.usecases.trip.TripFactory
import com.roadlink.tripservice.usecases.trip_application.plan.TripPlanApplicationFactory
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.uri.UriBuilder
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.JSONAssert
import java.util.*

@MicronautTest
class TripPlanApplicationHandlerE2ETest : End2EndTest() {

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Inject
    private lateinit var tripPlanApplicationRepository: TripPlanApplicationRepository

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
        val tripPlanApplicationId = UUID.randomUUID()
        val tripId = UUID.fromString(TripFactory.avCabildo_id)
        val tripApplicationId = UUID.randomUUID()
        val passengerId: UUID = UUID.randomUUID()
        givenASavedSection(tripId)
        givenASavedTripPlanApplication(
            tripPlanApplicationId = tripPlanApplicationId,
            passengerId = passengerId,
            tripApplicationId = tripApplicationId,
            tripApplicationStatus = TripApplication.Status.PENDING_APPROVAL
        )
        entityManager.transaction.commit()
        val request: HttpRequest<Any> = HttpRequest.GET(
            UriBuilder.of("/trip-service/trip_plan_application/$tripPlanApplicationId").build(),
        )

        // WHEN
        val response = client.toBlocking().exchange(request, JsonNode::class.java)

        // THEN
        JSONAssert.assertEquals(
            """
            {
              "id": "$tripPlanApplicationId",
              "tripApplications": [
                {
                  "id": "$tripApplicationId",
                  "passengerId": "$passengerId",
                  "sections": [
                    {
                      "id": "SectionFactory-avCabildo",
                      "tripId": "$tripId",
                      "departure": {
                        "estimatedArrivalTime": 1665835200,
                        "address": {
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

    private fun givenASavedTripPlanApplication(
        tripPlanApplicationId: UUID,
        tripApplicationId: UUID,
        passengerId: UUID = UUID.randomUUID(),
        tripApplicationStatus: TripApplication.Status = TripApplication.Status.PENDING_APPROVAL
    ) {
        tripPlanApplicationRepository.insert(
            TripPlanApplicationFactory.withASingleTripApplication(
                id = tripPlanApplicationId,
                tripApplicationId = tripApplicationId,
                passengerId = passengerId.toString(),
                tripApplicationStatus = tripApplicationStatus,
            )
        )
    }

    /**
     * List Trip Plan Application
     */
    @Test
    fun `listing all the trip plan applications by passenger id`() {
        // GIVEN
        val tripPlanApplicationId = UUID.randomUUID()
        val tripId = UUID.fromString(TripFactory.avCabildo_id)
        val passengerId = UUID.randomUUID()
        val tripApplicationId = UUID.randomUUID()
        givenASavedSection(tripId)
        givenASavedTripPlanApplication(tripPlanApplicationId, tripApplicationId, passengerId)
        entityManager.transaction.commit()

        val request: HttpRequest<Any> = HttpRequest.GET(
            UriBuilder.of("/trip-service/users/$passengerId/trip_plan_applications").build(),
        )

        // WHEN
        val response = client.toBlocking().exchange(request, JsonNode::class.java)

        // THEN
        JSONAssert.assertEquals(
            """
            [
               {
                  "id":"$tripPlanApplicationId",
                  "tripApplications":[
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

        val tripPlanApplicationId = UUID.randomUUID()
        val passengerId = UUID.randomUUID()
        val tripApplicationId = UUID.randomUUID()
        givenASavedTripPlanApplication(
            tripPlanApplicationId,
            tripApplicationId,
            passengerId,
            TripApplication.Status.REJECTED
        )

        val anotherTripPlanApplicationId = UUID.randomUUID()
        val anotherPassengerId = UUID.randomUUID()
        val anotherTripApplicationId = UUID.randomUUID()

        givenASavedTripPlanApplication(
            anotherTripPlanApplicationId,
            anotherPassengerId,
            anotherTripApplicationId,
            TripApplication.Status.PENDING_APPROVAL
        )

        entityManager.transaction.commit()

        val request: HttpRequest<Any> = HttpRequest.GET(
            UriBuilder.of("/trip-service/users/$passengerId/trip_plan_applications?status=REJECTED")
                .build(),
        )

        // WHEN
        val response = client.toBlocking().exchange(request, JsonNode::class.java)

        // THEN
        JSONAssert.assertEquals(
            """
            [
               {
                  "id":"$tripPlanApplicationId",
                  "tripApplications":[
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