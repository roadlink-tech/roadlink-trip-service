package com.roadlink.tripservice.domain.trip_application

import com.roadlink.tripservice.domain.trip.section.SectionError
import com.roadlink.tripservice.trip.domain.TripPlanApplicationFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.util.*

class TripPlanApplicationTest {

    @Test
    fun `when confirm an application, then the available seats must decrease by one`() {
        // GIVEN
        val id = UUID.randomUUID()
        val tripPlanApplication =
            TripPlanApplicationFactory.withASingleTripApplication(tripApplicationId = id, availableSeats = 4)

        // WHEN
        tripPlanApplication.confirmApplicationById(id)

        // THEN
        val section = tripPlanApplication.tripApplications.first().sections.first()
        assertEquals(3, section.availableSeats)
    }

    @Test
    fun `when try to confirm an application which does not have any available seat, then an exception must be retrieved`() {
        // GIVEN
        val id = UUID.randomUUID()
        val tripPlanApplication =
            TripPlanApplicationFactory.withoutAvailableSeats(tripApplicationId = id)

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
}