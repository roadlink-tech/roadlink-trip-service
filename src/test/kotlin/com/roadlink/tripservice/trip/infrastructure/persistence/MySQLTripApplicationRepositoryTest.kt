package com.roadlink.tripservice.trip.infrastructure.persistence

import com.roadlink.tripservice.domain.trip_application.TripPlanApplication
import com.roadlink.tripservice.infrastructure.persistence.MySQLSectionRepository
import com.roadlink.tripservice.infrastructure.persistence.trip_application.MySQLTripApplicationRepository
import com.roadlink.tripservice.infrastructure.persistence.trip_application.MySQLTripPlanApplicationRepository
import com.roadlink.tripservice.trip.domain.SectionFactory
import com.roadlink.tripservice.trip.domain.TripApplicationFactory
import com.roadlink.tripservice.trip.domain.TripPlanApplicationFactory
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.*

@MicronautTest
class MySQLTripApplicationRepositoryTest {

    @Inject
    private lateinit var sectionRepository: MySQLSectionRepository

    @Inject
    private lateinit var tripPlanApplicationRepository: MySQLTripPlanApplicationRepository

    @Inject
    private lateinit var tripApplicationRepository: MySQLTripApplicationRepository

    @Test
    fun `given no trip application exists with the given driver id when find all by driver id then should return empty list`() {
        val otherDriverId = UUID.randomUUID()
        givenExists(TripApplicationFactory.any())

        val result = tripApplicationRepository.findAllByDriverId(otherDriverId)

        assertTrue { result.isEmpty() }
    }

    @Test
    fun `given trip applications exists with the given driver id when find all by driver id then should return them`() {
        val driverId = UUID.randomUUID()
        val tripApplication1 = givenExists(TripApplicationFactory.withDriver(driverId))
        val tripApplication2 = givenExists(TripApplicationFactory.withDriver(driverId))

        val otherDriverId = UUID.randomUUID()
        val tripApplication3 = givenExists(TripApplicationFactory.withDriver(otherDriverId))

        val result = tripApplicationRepository.findAllByDriverId(driverId)

        assertEquals(2, result.size)
        assertTrue { result.containsAll(setOf(tripApplication1, tripApplication2)) }
    }

    @Test
    fun `given no trip application exists with the given trip id when find by trip id then should return empty list`() {
        val otherTripId = UUID.randomUUID()
        givenExists(TripApplicationFactory.any())

        val result = tripApplicationRepository.findByTripId(otherTripId)

        assertTrue { result.isEmpty() }
    }

    @Test
    fun `given trip applications exists with the given trip id when find by trip id then should return them`() {
        val avCabildoSection = SectionFactory.avCabildo()
        val tripApplication1 = givenExists(TripApplicationFactory.withSections(listOf(avCabildoSection)))
        val tripApplication2 = givenExists(TripApplicationFactory.withSections(listOf(avCabildoSection)))

        val virreyDelPinoSection = SectionFactory.virreyDelPino(tripId = UUID.randomUUID())
        val tripApplication3 = givenExists(TripApplicationFactory.withSections(listOf(virreyDelPinoSection)))

        val result = tripApplicationRepository.findByTripId(avCabildoSection.tripId)

        assertEquals(2, result.size)
        assertTrue { result.containsAll(setOf(tripApplication1, tripApplication2)) }
    }

    private fun givenExists(tripApplication: TripPlanApplication.TripApplication): TripPlanApplication.TripApplication {
        tripApplication.sections.forEach {
            if (sectionRepository.findAllById(setOf(it.id)).isEmpty())
                sectionRepository.save(it)
        }
        tripPlanApplicationRepository.insert(
            TripPlanApplicationFactory.withApplications(listOf(tripApplication))
        )

        return tripApplication
    }
}