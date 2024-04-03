package com.roadlink.tripservice.usecases.driver_trip

import com.roadlink.tripservice.domain.common.IdGenerator
import com.roadlink.tripservice.domain.trip.Trip
import com.roadlink.tripservice.domain.trip.TripRepository
import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.domain.trip_solicitude.TripLegSolicitudeRepository
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude.TripLegSolicitude.Status.ACCEPTED
import com.roadlink.tripservice.infrastructure.UUIDGenerator
import com.roadlink.tripservice.usecases.trip.TripFactory
import com.roadlink.tripservice.usecases.trip_solicitude.TripLegSolicitudeFactory
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class RetrieveDriverTripSummaryIntegrationTest {

    private lateinit var idGenerator: IdGenerator

    @MockK
    private lateinit var tripRepository: TripRepository

    @MockK
    private lateinit var sectionsRepository: SectionRepository

    @MockK
    private lateinit var tripLegSolicitudeRepository: TripLegSolicitudeRepository

    private lateinit var retrieveDriverTripSummary: RetrieveDriverTripSummary

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        idGenerator = UUIDGenerator()
        retrieveDriverTripSummary =
            RetrieveDriverTripSummary(
                tripRepository,
                sectionsRepository,
                tripLegSolicitudeRepository
            )
    }

    @AfterEach
    fun afterEach() {
        clearAllMocks()
    }

    @Test
    fun `when a trip was saved successfully, then its summary should be retrieved`() {
        // GIVEN
        val driverId = UUID.randomUUID()
        val trip = givenACreatedTrip(driverId = driverId)

        every { tripRepository.findAllByDriverId(driverId = driverId) } returns listOf(trip)

        every { sectionsRepository.findAllByTripIds(setOf(UUID.fromString(trip.id))) } returns trip.sections(
            idGenerator
        )

        every { tripLegSolicitudeRepository.find(match { it.driverId == driverId }) } returns listOf(
            TripLegSolicitudeFactory.common()
        )

        // WHEN
        val summary = retrieveDriverTripSummary(trip.driverId)

        // THEN
        assertNotNull(summary)
    }

    @Test
    fun `when the driver does not have any trip save, an empty summary must be retrieved`() {
        // GIVEN
        val driverId = UUID.randomUUID()
        every { tripRepository.findAllByDriverId(driverId = driverId) } returns listOf()

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

        every { tripRepository.findAllByDriverId(driverId = driverId) } returns listOf(trip)

        every { sectionsRepository.findAllByTripIds(setOf(UUID.fromString(trip.id))) } returns trip.sections(
            idGenerator
        )

        every { tripLegSolicitudeRepository.find(match { it.driverId == driverId }) } returns listOf(
            TripLegSolicitudeFactory.common()
        )

        // WHEN
        val summary = retrieveDriverTripSummary(driverId.toString())

        // THEN
        thenAtLeastATripWithoutCapacityMustBeRetrieved(summary)
    }

    @Test
    fun `when there is at least a trip leg solicitude accepted, then it must be retrieve in the summary`() {
        // given
        val driverId = UUID.randomUUID()
        val trip = givenACreatedTrip(driverId)

        every { tripRepository.findAllByDriverId(driverId = driverId) } returns listOf(trip)

        every { sectionsRepository.findAllByTripIds(setOf(UUID.fromString(trip.id))) } returns trip.sections(
            idGenerator
        )

        every { tripLegSolicitudeRepository.find(match { it.driverId == driverId }) } returns listOf(
            TripLegSolicitudeFactory.common(status = ACCEPTED)
        )

        // when
        val summary = retrieveDriverTripSummary(driverId.toString())

        // then
        assertTrue((summary as RetrieveDriverTripSummaryOutput.DriverTripSummariesFound).trips.any { !it.hasPendingApplications })
    }

    @Test
    fun `when there isn't any trip leg solicitude, then the summary retrieved must not contain any pending application`() {
        // GIVEN
        val driverId = UUID.randomUUID()
        val trip = givenACreatedTrip(driverId)

        every { tripRepository.findAllByDriverId(driverId = driverId) } returns listOf(trip)

        every { sectionsRepository.findAllByTripIds(setOf(UUID.fromString(trip.id))) } returns trip.sections(
            idGenerator
        )

        every { tripLegSolicitudeRepository.find(match { it.driverId == driverId }) } returns listOf()

        // WHEN
        val summary = retrieveDriverTripSummary(driverId.toString())

        // THEN
        assertTrue((summary as RetrieveDriverTripSummaryOutput.DriverTripSummariesFound).trips.any { !it.hasPendingApplications })
    }

    private fun thenAtLeastATripWithoutCapacityMustBeRetrieved(summary: RetrieveDriverTripSummaryOutput) {
        assertTrue((summary as RetrieveDriverTripSummaryOutput.DriverTripSummariesFound).trips.any { !it.hasAvailableSeats })
    }

    private fun givenACreatedTrip(
        driverId: UUID = UUID.fromString("81dcb088-4b7e-4956-a50a-52eee0dd5a0f"),
        availableSeats: Int = 4
    ): Trip {
        return TripFactory.avCabildo4853_to_avCabildo20(
            driverId = driverId.toString(),
            availableSeats = availableSeats
        )
    }
}