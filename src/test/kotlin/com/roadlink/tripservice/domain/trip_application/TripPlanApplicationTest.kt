package com.roadlink.tripservice.domain.trip_application

import com.roadlink.tripservice.domain.trip.section.SectionError
import com.roadlink.tripservice.trip.domain.TripPlanApplicationFactory
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

class TripPlanApplicationTest {

    @Test
    fun `when confirm an application, then the available seats must decrease by one`() {
        // GIVEN
        val id = UUID.randomUUID()
        val tripPlanApplication =
            TripPlanApplicationFactory.withASingleTripApplication(tripApplicationId = id, initialAmountOfSeats = 4)

        // WHEN
        tripPlanApplication.confirmApplicationById(id)

        // THEN
        val section = tripPlanApplication.tripApplications.first().sections.first()
        assertEquals(3, section.availableSeats())
    }

    @Test
    fun `when confirm an application twice, then the available seats must decrease by 2`() {
        // GIVEN
        val id = UUID.randomUUID()
        val tripPlanApplication =
            TripPlanApplicationFactory.withASingleTripApplication(tripApplicationId = id, initialAmountOfSeats = 4)

        // WHEN
        repeat(2) {
            tripPlanApplication.confirmApplicationById(id)
        }

        // THEN
        val section = tripPlanApplication.tripApplications.first().sections.first()
        assertEquals(2, section.availableSeats())
    }

    @Test
    fun `when try to confirm an application which does not have any available seat, then an exception must be retrieved`() {
        // GIVEN
        val id = UUID.randomUUID()
        val tripPlanApplication =
            TripPlanApplicationFactory.completed(tripApplicationId = id)

        // WHEN
        assertThrows(SectionError.InsufficientAvailableSeats::class.java) {
            tripPlanApplication.confirmApplicationById(id)
        }
    }

    @Test
    fun `when try to confirm an application that not exist, then an exception must be retrieved`() {
        // GIVEN
        val id = UUID.randomUUID()
        val tripPlanApplication = TripPlanApplicationFactory.withASingleTripApplication()

        // WHEN
        assertThrows(TripApplicationError.NotFound::class.java) {
            tripPlanApplication.confirmApplicationById(id)
        }
    }

    @Test
    fun `when reject an application which does not have any booking, then it must work`() {
        // GIVEN
        val tripPlanApplication = TripPlanApplicationFactory.withASingleTripApplication()

        // WHEN
        tripPlanApplication.reject()

        // THEN
        assertTrue(tripPlanApplication.isRejected())
    }

}