package com.roadlink.tripservice.infrastructure.persistence.trip

import com.roadlink.tripservice.domain.common.utils.time.TimeRange
import com.roadlink.tripservice.domain.trip.TripRepository
import com.roadlink.tripservice.domain.trip.constraint.Rule
import com.roadlink.tripservice.domain.trip.constraint.Visibility
import com.roadlink.tripservice.usecases.common.InstantFactory
import com.roadlink.tripservice.usecases.trip.TripFactory
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

@MicronautTest
class MySQLTripRepositoryTest {

    @Inject
    private lateinit var repository: TripRepository

    @Test
    fun `can save trip with no meeting points`() {
        // given
        val driverId = UUID.randomUUID()
        val trip = TripFactory.common(driverId = driverId)

        // when
        repository.save(trip)

        // then
        assertEquals(
            listOf(trip),
            repository.find(commandQuery = TripRepository.CommandQuery(driverId = driverId))
        )
    }

    @Test
    fun `when save a trip with policies and restrictions, then it must be retrieved`() {
        // given
        val driverId = UUID.randomUUID()
        val trip = TripFactory.common(
            driverId = driverId,
            policies = listOf(Rule.NoSmoking, Rule.PetAllowed),
            restrictions = listOf(Visibility.OnlyFriends, Visibility.OnlyWomen)
        )
        repository.save(trip)

        // when
        val result =
            repository.find(commandQuery = TripRepository.CommandQuery(driverId = driverId)).first()
        // then
        assertEquals(trip, result)
        assertTrue(
            result.restrictions.containsAll(
                listOf(
                    Visibility.OnlyFriends,
                    Visibility.OnlyWomen
                )
            )
        )
        assertTrue(result.policies.containsAll(listOf(Rule.NoSmoking, Rule.PetAllowed)))
    }

    @Test
    fun `when save a trip without any policy and restriction, then it must be retrieved as empty values`() {
        // given
        val driverId = UUID.randomUUID()
        val trip = TripFactory.common(driverId = driverId)
        repository.save(trip)

        // when
        val result =
            repository.find(commandQuery = TripRepository.CommandQuery(driverId = driverId)).first()
        // then
        assertEquals(trip, result)
        assertTrue(result.restrictions.isEmpty())
        assertTrue(result.policies.isEmpty())
    }

    @Test
    fun `can save trip with one meeting point`() {
        // given
        val driverId = UUID.randomUUID()
        val trip =
            TripFactory.avCabildo4853_virreyDelPino1800_avCabildo20(driverId = driverId.toString())

        // when
        repository.save(trip)

        // then
        assertEquals(
            listOf(trip),
            repository.find(commandQuery = TripRepository.CommandQuery(driverId = driverId))
        )
    }

    @Test
    fun `can save trip with multiple meeting points`() {
        val driverId = UUID.randomUUID()
        val trip = TripFactory.caba_escobar_pilar_rosario(driverId = driverId.toString())

        repository.save(trip)

        assertEquals(
            listOf(trip),
            repository.find(commandQuery = TripRepository.CommandQuery(driverId = driverId))
        )
    }

    @Test
    fun `given no trip exists with the given driver id when find all by driver id then should return empty list`() {
        val otherDriverId = UUID.randomUUID()
        val trip = TripFactory.caba_escobar_pilar_rosario()
        repository.save(trip)

        val result =
            repository.find(commandQuery = TripRepository.CommandQuery(driverId = otherDriverId))

        assertTrue { result.isEmpty() }
    }

    @Test
    fun `given trip applications exists with the given driver id when find all by driver id then should return them`() {
        val driverId = UUID.randomUUID()
        val trip1 = TripFactory.avCabildo4853_to_avCabildo20(driverId = driverId.toString())
        val trip2 =
            TripFactory.avCabildo4853_virreyDelPino1800_avCabildo20(driverId = driverId.toString())
        repository.save(trip1)
        repository.save(trip2)

        val otherDriverId = UUID.randomUUID()
        val trip3 = TripFactory.caba_escobar_pilar_rosario(driverId = otherDriverId.toString())
        repository.save(trip3)

        val result =
            repository.find(commandQuery = TripRepository.CommandQuery(driverId = driverId))

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