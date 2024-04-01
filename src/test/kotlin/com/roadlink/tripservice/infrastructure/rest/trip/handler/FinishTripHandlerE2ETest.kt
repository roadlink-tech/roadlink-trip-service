package com.roadlink.tripservice.infrastructure.rest.trip.handler

import com.fasterxml.jackson.databind.JsonNode
import com.roadlink.tripservice.domain.trip_plan.TripPlan.*
import com.roadlink.tripservice.domain.trip_plan.TripPlan.Status.*
import com.roadlink.tripservice.domain.trip_plan.TripPlanRepository
import com.roadlink.tripservice.infrastructure.End2EndTest
import com.roadlink.tripservice.usecases.common.trip_point.TripPointFactory
import com.roadlink.tripservice.usecases.trip_plan.TripLegSectionFactory
import com.roadlink.tripservice.usecases.trip_plan.TripPlanFactory
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.uri.UriBuilder
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

@MicronautTest
internal class FinishTripHandlerE2ETest : End2EndTest() {

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Inject
    private lateinit var tripPlanRepository: TripPlanRepository

    @Test
    fun `can create trip with meeting points`() {
        // given
        val oneTripId = UUID.randomUUID()
        val oneTripLeg = TripLeg(
            id = UUID.randomUUID(),
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
        val twoTripId = UUID.randomUUID()
        val twoTripLeg = TripLeg(
            id = UUID.randomUUID(),
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
        val tripPlan = TripPlanFactory.common(tripLegs = listOf(oneTripLeg, twoTripLeg))
        tripPlanRepository.insert(tripPlan)
        entityManager.transaction.commit()

        val request = HttpRequest.POST(
            UriBuilder.of("/trip-service/trips/$oneTripId/finish").build(), ""
        )

        // when
        val response = client.toBlocking().exchange(request, JsonNode::class.java)

    }
}