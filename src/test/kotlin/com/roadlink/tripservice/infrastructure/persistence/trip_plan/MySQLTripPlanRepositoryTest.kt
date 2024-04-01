package com.roadlink.tripservice.infrastructure.persistence.trip_plan

import com.roadlink.tripservice.domain.trip_plan.TripLegSection
import com.roadlink.tripservice.domain.trip_plan.TripPlan
import com.roadlink.tripservice.domain.trip_plan.TripPlanRepository
import com.roadlink.tripservice.usecases.common.trip_point.TripPointFactory
import com.roadlink.tripservice.usecases.trip_plan.TripPlanFactory
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.*

@MicronautTest
class MySQLTripPlanRepositoryTest {

    @Inject
    private lateinit var tripPlanRepository: TripPlanRepository

    @Test
    fun `given a saved trip plan, when find it by id then it must be retrieved`() {
        // given
        val tripPlanId = UUID.randomUUID()
        val driverId = UUID.randomUUID()
        val passengerId = UUID.randomUUID()
        val tripLegId = UUID.randomUUID()
        val tripId = UUID.randomUUID()
        val vehicleId = UUID.randomUUID()
        val status = TripPlan.Status.NOT_FINISHED
        tripPlanRepository.insert(
            TripPlanFactory.withASingleTripLeg(
                id = tripPlanId,
                passengerId = passengerId,
                driverId = driverId,
                tripLegId = tripLegId,
                tripId = tripId,
                vehicleId = vehicleId,
                status = status,
                sections = listOf(
                    TripLegSection(
                        id = UUID.randomUUID().toString(),
                        departure = TripPointFactory.avCabildo_20(),
                        arrival = TripPointFactory.avCabildo_4853(),
                        distanceInMeters = 0.0,
                    ),
                )
            )
        )

        // when
        val result = tripPlanRepository.find(TripPlanRepository.CommandQuery(id = tripPlanId))

        // then
        assertTrue { result.isNotEmpty() }
        assertTrue { result.map { it.id }.contains(tripPlanId) }
        assertTrue {
            result.first().id == tripPlanId &&
                    result.first().passengerId == passengerId &&
                    result.first().tripLegs.first().id == tripLegId &&
                    result.first().tripLegs.first().driverId == driverId &&
                    result.first().tripLegs.first().tripId == tripId &&
                    result.first().tripLegs.first().vehicleId == vehicleId &&
                    result.first().tripLegs.first().status == status
        }
    }

    @Test
    fun `given a saved trip plan, when find it by passenger id then it must be retrieved`() {
        // given
        val tripPlanId = UUID.randomUUID()
        val driverId = UUID.randomUUID()
        val passengerId = UUID.randomUUID()
        val tripLegId = UUID.randomUUID()
        val tripId = UUID.randomUUID()
        val vehicleId = UUID.randomUUID()
        val status = TripPlan.Status.NOT_FINISHED
        tripPlanRepository.insert(
            TripPlanFactory.withASingleTripLeg(
                id = tripPlanId,
                passengerId = passengerId,
                driverId = driverId,
                tripLegId = tripLegId,
                tripId = tripId,
                vehicleId = vehicleId,
                status = status
            )
        )

        // when
        val result = tripPlanRepository.find(TripPlanRepository.CommandQuery(passengerId = passengerId))

        // then
        assertTrue { result.isNotEmpty() }
        assertTrue { result.map { it.id }.contains(tripPlanId) }
        assertTrue {
            result.first().id == tripPlanId &&
                    result.first().passengerId == passengerId &&
                    result.first().tripLegs.first().id == tripLegId &&
                    result.first().tripLegs.first().driverId == driverId &&
                    result.first().tripLegs.first().tripId == tripId &&
                    result.first().tripLegs.first().vehicleId == vehicleId &&
                    result.first().tripLegs.first().status == status
        }
    }

}