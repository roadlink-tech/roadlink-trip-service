package com.roadlink.tripservice.domain.trip_solicitude

import com.roadlink.tripservice.domain.trip.section.SectionError
import com.roadlink.tripservice.usecases.trip_solicitude.plan.TripPlanSolicitudeFactory
import com.roadlink.tripservice.usecases.trip_solicitude.plan.TripPlanSolicitudeFactory.johnSmithDriverId
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

class TripPlanSolicitudeTest {

    @Test
    fun `when confirm an application, then the available seats must decrease by one`() {
        // GIVEN
        val id = UUID.randomUUID()
        val callerId = UUID.randomUUID()
        val tripPlanSolicitude =
            TripPlanSolicitudeFactory.withASingleTripApplication(tripApplicationId = id, initialAmountOfSeats = 4)

        // WHEN
        tripPlanSolicitude.confirmApplicationById(id, callerId)

        // THEN
        val section = tripPlanSolicitude.tripLegSolicitudes.first().sections.first()
        assertEquals(3, section.availableSeats())
    }

    @Test
    fun `when try to confirm an application but the caller is a driver of the trip application, then an error must be thrown`() {
        // GIVEN
        val id = UUID.randomUUID()
        val driverId = UUID.randomUUID()
        val tripPlanSolicitude =
            TripPlanSolicitudeFactory.withASingleTripApplication(
                tripApplicationId = id,
                initialAmountOfSeats = 4,
                driverId = driverId.toString()
            )

        // WHEN
        val ex = assertThrows(TripApplicationError.DriverTryingToJoinAsPassenger::class.java) {
            tripPlanSolicitude.confirmApplicationById(id, driverId)
        }

        // THEN
        assertNotNull(ex)
        assertTrue(ex.message.contains("is driver and is trying to join to"))
    }

    @Test
    fun `when confirm an application twice, then the available seats must decrease by 2`() {
        // GIVEN
        val id = UUID.randomUUID()
        val callerId = UUID.randomUUID()
        val tripPlanSolicitude =
            TripPlanSolicitudeFactory.withASingleTripApplication(tripApplicationId = id, initialAmountOfSeats = 4)

        // WHEN
        repeat(2) {
            tripPlanSolicitude.confirmApplicationById(id, callerId)
        }

        // THEN
        val section = tripPlanSolicitude.tripLegSolicitudes.first().sections.first()
        assertEquals(2, section.availableSeats())
    }

    @Test
    fun `when try to confirm an application which does not have any available seat, then an exception must be retrieved`() {
        // GIVEN
        val id = UUID.randomUUID()
        val callerId = UUID.randomUUID()
        val tripPlanSolicitude =
            TripPlanSolicitudeFactory.completed(tripApplicationId = id)

        // WHEN
        assertThrows(SectionError.InsufficientAvailableSeats::class.java) {
            tripPlanSolicitude.confirmApplicationById(id, callerId)
        }
    }

    @Test
    fun `when try to confirm an application that not exist, then an exception must be retrieved`() {
        // GIVEN
        val id = UUID.randomUUID()
        val tripPlanSolicitude = TripPlanSolicitudeFactory.withASingleTripApplication()

        // WHEN
        assertThrows(TripApplicationError.NotFound::class.java) {
            tripPlanSolicitude.confirmApplicationById(id, johnSmithDriverId)
        }
    }

    @Test
    fun `when reject an application which does not have any booking, then it must work`() {
        // GIVEN
        val tripPlanSolicitude = TripPlanSolicitudeFactory.withASingleTripApplication()

        // WHEN
        tripPlanSolicitude.reject()

        // THEN
        assertTrue(tripPlanSolicitude.isRejected())
    }

}