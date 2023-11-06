package com.roadlink.tripservice.trip.infrastructure.persistence

import com.roadlink.tripservice.infrastructure.persistence.MySQLTripRepository
import com.roadlink.tripservice.trip.domain.TripFactory
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.UUID

@MicronautTest
class MySQLTripRepositoryTest {

    @Inject
    private lateinit var repository: MySQLTripRepository

    @Test
    fun `can save trip with no meeting points`() {
        val driverId = UUID.randomUUID()
        val trip = TripFactory.avCabildo4853_to_avCabildo20(driverId = driverId.toString())

        repository.save(trip)

        assertEquals(listOf(trip), repository.findAllByDriverId(driverId))
    }

    @Test
    fun `can save trip with one meeting point`() {
        val driverId = UUID.randomUUID()
        val trip = TripFactory.avCabildo4853_virreyDelPino1800_avCabildo20(driverId = driverId.toString())

        repository.save(trip)

        assertEquals(listOf(trip), repository.findAllByDriverId(driverId))
    }

    @Test
    fun `can save trip with multiple meeting points`() {
        val driverId = UUID.randomUUID()
        val trip = TripFactory.caba_escobar_pilar_rosario(driverId = driverId.toString())

        repository.save(trip)

        assertEquals(listOf(trip), repository.findAllByDriverId(driverId))
    }

    @Test
    fun `given no trip exists with the given driver id when find all by driver id then should return empty list`() {
        val otherDriverId = UUID.randomUUID()
        val trip = TripFactory.caba_escobar_pilar_rosario()
        repository.save(trip)

        val result = repository.findAllByDriverId(otherDriverId)

        assertTrue { result.isEmpty() }
    }

    @Test
    fun `given trip applications exists with the given driver id when find all by driver id then should return them`() {
        val driverId = UUID.randomUUID()
        val trip1 = TripFactory.avCabildo4853_to_avCabildo20(driverId = driverId.toString())
        val trip2 = TripFactory.avCabildo4853_virreyDelPino1800_avCabildo20(driverId = driverId.toString())
        repository.save(trip1)
        repository.save(trip2)

        val otherDriverId = UUID.randomUUID()
        val trip3 = TripFactory.caba_escobar_pilar_rosario(driverId = otherDriverId.toString())
        repository.save(trip3)

        val result = repository.findAllByDriverId(driverId)

        assertEquals(2, result.size)
        assertTrue { result.containsAll(setOf(trip1, trip2)) }
    }
}