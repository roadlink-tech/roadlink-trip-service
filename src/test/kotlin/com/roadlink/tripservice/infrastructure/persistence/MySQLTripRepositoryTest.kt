package com.roadlink.tripservice.infrastructure.persistence

import com.roadlink.tripservice.domain.common.utils.time.TimeRange
import com.roadlink.tripservice.domain.trip.TripRepository
import com.roadlink.tripservice.usecases.factory.InstantFactory
import com.roadlink.tripservice.usecases.trip.TripFactory
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.UUID

@MicronautTest
class MySQLTripRepositoryTest {

    @Inject
    private lateinit var repository: TripRepository

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

    @Test
    fun `given exists trip in time range but for other driver when exists by driver and in time range then should return false`() {
        val driverId = UUID.randomUUID().toString()
        val timeRange = TimeRange(
            from = InstantFactory.october15_13hs(),
            to = InstantFactory.october15_15hs(),
        )

        val otherDriverId = UUID.randomUUID().toString()
        val trip = TripFactory.avCabildo4853_to_avCabildo20(driverId = otherDriverId)
        repository.save(trip)

        val result = repository.existsByDriverAndInTimeRange(driverId, timeRange)

        assertFalse { result }
    }

    @Test
    fun `given exists trip in time range for driver when exists by driver and in time range then should return true`() {
        val driverId = UUID.randomUUID().toString()
        val timeRange = TimeRange(
            from = InstantFactory.october15_13hs(),
            to = InstantFactory.october15_15hs(),
        )

        val trip = TripFactory.avCabildo4853_to_avCabildo20(driverId = driverId)
        repository.save(trip)

        val result = repository.existsByDriverAndInTimeRange(driverId, timeRange)

        assertTrue { result }
    }

    @Test
    fun `given exists trip but after time range when exists by driver and in time range then should return false`() {
        val driverId = UUID.randomUUID().toString()
        val timeRange = TimeRange(
            from = InstantFactory.october15_7hs(),
            to = InstantFactory.october15_9hs(),
        )

        val trip = TripFactory.avCabildo4853_to_avCabildo20(driverId = driverId)
        repository.save(trip)

        val result = repository.existsByDriverAndInTimeRange(driverId, timeRange)

        assertFalse { result }
    }

    @Test
    fun `given exists trip but before time range when exists by driver and in time range then should return false`() {
        val driverId = UUID.randomUUID().toString()
        val timeRange = TimeRange(
            from = InstantFactory.october15_20hs(),
            to = InstantFactory.october15_22hs(),
        )

        val trip = TripFactory.avCabildo4853_to_avCabildo20(driverId = driverId)
        repository.save(trip)

        val result = repository.existsByDriverAndInTimeRange(driverId, timeRange)

        assertFalse { result }
    }

    @Test
    fun `given exists trip with departure between time range when exists by driver and in time range then should return true`() {
        val driverId = UUID.randomUUID().toString()
        val timeRange = TimeRange(
            from = InstantFactory.october15_9hs(),
            to = InstantFactory.october15_13hs(),
        )

        val trip = TripFactory.avCabildo4853_to_avCabildo20(driverId = driverId)
        repository.save(trip)

        val result = repository.existsByDriverAndInTimeRange(driverId, timeRange)

        assertTrue { result }
    }

    @Test
    fun `given exists trip with arrival between time range when exists by driver and in time range then should return true`() {
        val driverId = UUID.randomUUID().toString()
        val timeRange = TimeRange(
            from = InstantFactory.october15_17hs(),
            to = InstantFactory.october15_20hs(),
        )

        val trip = TripFactory.avCabildo4853_to_avCabildo20(driverId = driverId)
        repository.save(trip)

        val result = repository.existsByDriverAndInTimeRange(driverId, timeRange)

        assertTrue { result }
    }
}