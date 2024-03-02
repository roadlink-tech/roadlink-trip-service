package com.roadlink.tripservice.usecases.driver_trip

import com.roadlink.tripservice.domain.IdGenerator
import com.roadlink.tripservice.domain.trip.Trip
import com.roadlink.tripservice.domain.trip.TripRepository
import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.domain.trip_application.TripApplicationRepository
import com.roadlink.tripservice.domain.trip_application.TripPlanApplication.TripApplication.*
import com.roadlink.tripservice.domain.trip_application.TripPlanApplication.TripApplication.Status.*
import com.roadlink.tripservice.infrastructure.UUIDIdGenerator
import com.roadlink.tripservice.infrastructure.persistence.InMemorySectionRepository
import com.roadlink.tripservice.infrastructure.persistence.InMemoryTripRepository
import com.roadlink.tripservice.infrastructure.persistence.trip_application.InMemoryTripApplicationRepository
import com.roadlink.tripservice.infrastructure.persistence.trip_application.InMemoryTripPlanApplicationRepository
import com.roadlink.tripservice.usecases.trip.TripFactory
import com.roadlink.tripservice.usecases.trip_application.plan.TripPlanApplicationFactory
import org.junit.jupiter.api.AfterEach
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
        val driverId = UUID.randomUUID()
        val trip = givenACreatedTrip(driverId = driverId)
        givenATripPlanApplicationFor(trip.id, driverId)

        // WHEN
        val summary = retrieveDriverTripSummary(trip.driverId)

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
        assertInstanceOf(RetrieveDriverTripSummaryOutput::class.java, summary)
    }

    @Test
    fun `when a trip can not receive any passenger, then it must be retrieved in the summary response`() {
        // GIVEN
        val driverId = UUID.randomUUID()
        val trip = givenACreatedTrip(driverId, availableSeats = 0)
        givenATripPlanApplicationFor(trip.id, driverId)

        // WHEN
        val summary = retrieveDriverTripSummary(driverId.toString())

        // THEN
        thenAtLeastATripWithoutCapacityMustBeRetrieved(summary)
    }

    @Test
    fun `when there are more than one trip, all of those must be retrieve in the summary`() {
        // GIVEN
        val driverId = UUID.randomUUID()
        repeat(5) {
            val trip = givenACreatedTrip(driverId)
            givenATripPlanApplicationFor(trip.id, driverId)
        }

        // WHEN
        val summary = retrieveDriverTripSummary(driverId.toString())

        // THEN
        thenAllTheTripApplicationsMustBeRetrieved(summary)
    }

    @Test
    fun `when there is at least a trip application plan confirm, then it must be retrieve in the summary`() {
        // GIVEN
        val driverId = UUID.randomUUID()

        val trip = givenACreatedTrip(driverId)
        givenATripPlanApplicationFor(trip.id, driverId, CONFIRMED)

        // WHEN
        val summary = retrieveDriverTripSummary(driverId.toString())

        // THEN
        assertTrue((summary as RetrieveDriverTripSummaryOutput.DriverTripSummariesFound).trips.any { !it.hasPendingApplications })
    }

    @Test
    fun `when there isn't any trip application, then the summary retrieved must not contain any pending application`() {
        // GIVEN
        val driverId = UUID.randomUUID()
        givenACreatedTrip(driverId)

        // WHEN
        val summary = retrieveDriverTripSummary(driverId.toString())

        // THEN
        assertTrue((summary as RetrieveDriverTripSummaryOutput.DriverTripSummariesFound).trips.any { !it.hasPendingApplications })
    }

    private fun thenAllTheTripApplicationsMustBeRetrieved(summary: RetrieveDriverTripSummaryOutput) {
        assertTrue((summary as RetrieveDriverTripSummaryOutput.DriverTripSummariesFound).trips.size == 5)
    }

    private fun thenAtLeastATripWithoutCapacityMustBeRetrieved(summary: RetrieveDriverTripSummaryOutput) {
        assertTrue((summary as RetrieveDriverTripSummaryOutput.DriverTripSummariesFound).trips.any { !it.hasAvailableSeats })
    }

    private fun givenATripPlanApplicationFor(
        tripId: String,
        driverId: UUID,
        status: Status = PENDING_APPROVAL
    ) {
        val tripPlanApplication = TripPlanApplicationFactory.withASingleBooking(
            tripId = UUID.fromString(tripId),
            driverId = driverId.toString(),
            status = status
        )
        tripPlanApplicationRepository.insert(tripPlanApplication)
    }

    private fun givenACreatedTrip(
        driverId: UUID = UUID.fromString("81dcb088-4b7e-4956-a50a-52eee0dd5a0f"),
        availableSeats: Int = 4
    ): Trip {
        val trip =
            TripFactory.avCabildo4853_to_avCabildo20(driverId = driverId.toString(), availableSeats = availableSeats)
        tripRepository.save(trip)
        sectionsRepository.saveAll(trip.sections(idGenerator))
        return trip
    }
}