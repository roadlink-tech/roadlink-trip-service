package com.roadlink.tripservice.usecases.trip_summary

import com.roadlink.tripservice.domain.IdGenerator
import com.roadlink.tripservice.domain.trip.Trip
import com.roadlink.tripservice.domain.trip.TripRepository
import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.domain.trip_application.TripApplicationRepository
import com.roadlink.tripservice.infrastructure.UUIDIdGenerator
import com.roadlink.tripservice.infrastructure.persistence.InMemorySectionRepository
import com.roadlink.tripservice.infrastructure.persistence.InMemoryTripRepository
import com.roadlink.tripservice.infrastructure.persistence.trip_application.InMemoryTripApplicationRepository
import com.roadlink.tripservice.infrastructure.persistence.trip_application.InMemoryTripPlanApplicationRepository
import com.roadlink.tripservice.trip.domain.TripFactory
import com.roadlink.tripservice.trip.domain.TripPlanApplicationFactory
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class RetrieveDriverTripSummaryIntegrationTest {

    private lateinit var idGenerator: IdGenerator

    private lateinit var tripRepository: TripRepository

    private lateinit var sectionsRepository: SectionRepository

    private lateinit var tripPlanApplicationRepository: InMemoryTripPlanApplicationRepository

    private lateinit var tripApplicationRepository: TripApplicationRepository

    private lateinit var retrieveDriverTripSummary: RetrieveDriverTripSummary

    @BeforeEach
    fun setUp() {
        idGenerator = UUIDIdGenerator()
        tripRepository = InMemoryTripRepository()
        sectionsRepository = InMemorySectionRepository()
        tripApplicationRepository = InMemoryTripApplicationRepository()
        tripPlanApplicationRepository =
            InMemoryTripPlanApplicationRepository(tripApplicationRepository = tripApplicationRepository)
        retrieveDriverTripSummary =
            RetrieveDriverTripSummary(tripRepository, sectionsRepository, tripApplicationRepository)
    }

    @AfterEach
    fun afterEach() {
        tripPlanApplicationRepository.deleteAll()
    }

    @Test
    fun `when a trip was saved successfully, then its summary should be retrieved`() {
        // GIVEN
        val trip = givenACreatedTrip()
        givenATripPlanApplicationPendingFor(trip)

        // WHEN
        val summary = retrieveDriverTripSummary(trip.driver)

        // THEN
        assertNotNull(summary)
    }

    @Test
    fun `when the driver does not have any trip save, an empty summary must be retrieved`() {
        // GIVEN
        val driverId = UUID.randomUUID()

        // WHEN
        val summary = retrieveDriverTripSummary(driverId.toString())

        // THEN
        assertTrue(summary.thereAreNotTrips())
    }

    private fun givenATripPlanApplicationPendingFor(trip: Trip) {
        val tripPlanApplication = TripPlanApplicationFactory.withASingleBooking(
            tripId = UUID.fromString(trip.id),
            driverId = "81dcb088-4b7e-4956-a50a-52eee0dd5a0f"
        )
        tripPlanApplicationRepository.save(tripPlanApplication)
    }

    private fun givenACreatedTrip(): Trip {
        val trip = TripFactory.avCabildo4853_to_avCabildo20(driverId = "81dcb088-4b7e-4956-a50a-52eee0dd5a0f")
        tripRepository.save(trip)
        sectionsRepository.save(trip.sections(idGenerator))
        return trip
    }
}