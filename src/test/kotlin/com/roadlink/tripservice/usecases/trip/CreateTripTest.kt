package com.roadlink.tripservice.usecases.trip

import com.roadlink.tripservice.config.SpyCommandBus
import com.roadlink.tripservice.config.StubCreateTripHandler
import com.roadlink.tripservice.config.StubIdGenerator
import com.roadlink.tripservice.config.StubTimeProvider
import com.roadlink.tripservice.domain.common.utils.time.exception.InvalidTripTimeRange
import com.roadlink.tripservice.domain.trip.TripRepository
import com.roadlink.tripservice.domain.trip.exception.AlreadyExistsTripByDriverInTimeRange
import com.roadlink.tripservice.usecases.common.InstantFactory
import com.roadlink.tripservice.usecases.common.trip_point.TripPointFactory
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class CreateTripTest {

    @MockK
    private lateinit var tripRepository: TripRepository

    private lateinit var stubIdGenerator: StubIdGenerator

    private lateinit var stubTimeProvider: StubTimeProvider

    private lateinit var commandBus: SpyCommandBus

    private lateinit var createTrip: CreateTrip

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        stubIdGenerator = StubIdGenerator()
        stubTimeProvider = StubTimeProvider(fixedNow = InstantFactory.october15_7hs())
        commandBus = SpyCommandBus()
        commandBus.registerHandler(StubCreateTripHandler())

        createTrip = CreateTrip(
            tripRepository = tripRepository,
            idGenerator = stubIdGenerator,
            commandBus = commandBus,
            timeProvider = stubTimeProvider,
        )
    }

    @AfterEach
    fun clear() {
        commandBus.clear()
    }

    @Test
    fun `given already exists trip with same driver in the given time range then should fail`() {
        // given
        every { tripRepository.save(trip = match { it == TripFactory.avCabildo4853_virreyDelPino1800_avCabildo20() }) } just runs

        every {
            tripRepository.existsByDriverAndInTimeRange(
                driverId = match { it == TripFactory.avCabildo4853_virreyDelPino1800_avCabildo20().driverId },
                any()
            )
        } returns true

        //when
        assertThrows<AlreadyExistsTripByDriverInTimeRange> {
            createTrip(
                CreateTrip.Input(
                    driver = "John Smith",
                    vehicle = "Ford mustang",
                    departure = TripPointFactory.avCabildo_4853(),
                    arrival = TripPointFactory.avCabildo_20(),
                    meetingPoints = emptyList(),
                    availableSeats = 4,
                    policies = emptyList(),
                    restrictions = emptyList()
                )
            )
        }

        // then
        theCommandHasNotBeenPublished()
    }

    @Test
    fun `given trip with no meeting points and arrival at before departure at then should fail`() {
        // when
        assertThrows<InvalidTripTimeRange> {
            createTrip(
                CreateTrip.Input(
                    driver = "John Smith",
                    vehicle = "Ford mustang",
                    departure = TripPointFactory.avCabildo_4853(),
                    arrival = TripPointFactory.avCabildo_20(
                        at = InstantFactory.october15_7hs(),
                    ),
                    meetingPoints = emptyList(),
                    availableSeats = 4,
                    policies = emptyList(),
                    restrictions = emptyList()
                )
            )
        }

        //then
        theCommandHasNotBeenPublished()
    }

    @Test
    fun `given trip with one meeting point and meeting point at before departureAt then should fail`() {
        // when
        assertThrows<InvalidTripTimeRange> {
            createTrip(
                CreateTrip.Input(
                    driver = "John Smith",
                    vehicle = "Ford mustang",
                    departure = TripPointFactory.avCabildo_4853(),
                    arrival = TripPointFactory.avCabildo_20(),
                    meetingPoints = listOf(
                        TripPointFactory.virreyDelPino_1800(
                            at = InstantFactory.october15_7hs(),
                        )
                    ),
                    availableSeats = 4,
                    policies = emptyList(),
                    restrictions = emptyList()
                )
            )
        }

        // then
        theCommandHasNotBeenPublished()
    }

    @Test
    fun `given trip with one meeting point and meeting point at after arrival at then should fail`() {
        // given
        assertThrows<InvalidTripTimeRange> {
            createTrip(
                CreateTrip.Input(
                    driver = "John Smith",
                    vehicle = "Ford mustang",
                    departure = TripPointFactory.avCabildo_4853(),
                    arrival = TripPointFactory.avCabildo_20(),
                    meetingPoints = listOf(
                        TripPointFactory.virreyDelPino_1800(
                            at = InstantFactory.october15_22hs(),
                        )
                    ),
                    availableSeats = 4,
                    policies = emptyList(),
                    restrictions = emptyList()
                )
            )
        }

        // then
        theCommandHasNotBeenPublished()
    }

    @Test
    fun `can create trip with no meeting points`() {
        // given
        stubIdGenerator.nextIdToGenerate(id = TripFactory.avCabildo_id)

        every {
            tripRepository.existsByDriverAndInTimeRange(
                driverId = any(),
                timeRange = any()
            )
        } returns false

        every { tripRepository.save(trip = match { it.id == TripFactory.avCabildo_id }) } just runs

        // when
        val result = createTrip(
            CreateTrip.Input(
                driver = "John Smith",
                vehicle = "Ford mustang",
                departure = TripPointFactory.avCabildo_4853(),
                arrival = TripPointFactory.avCabildo_20(),
                meetingPoints = emptyList(),
                availableSeats = 4,
                policies = emptyList(),
                restrictions = emptyList()
            )
        )

        // then
        assertEquals(TripFactory.avCabildo4853_to_avCabildo20(), result)
        theCommandHasBeenPublished()
    }

    @Test
    fun `can create trip with meeting points`() {
        // given
        stubIdGenerator.nextIdToGenerate(id = TripFactory.avCabildo4853_virreyDelPino1800_avCabildo20_id)
        every {
            tripRepository.existsByDriverAndInTimeRange(
                driverId = any(),
                timeRange = any()
            )
        } returns false

        every { tripRepository.save(trip = match { it.id == TripFactory.avCabildo4853_virreyDelPino1800_avCabildo20_id }) } just runs

        // when
        val result = createTrip(
            CreateTrip.Input(
                driver = "John Smith",
                vehicle = "Ford mustang",
                departure = TripPointFactory.avCabildo_4853(),
                arrival = TripPointFactory.avCabildo_20(),
                meetingPoints = listOf(TripPointFactory.virreyDelPino_1800()),
                availableSeats = 5,
                policies = emptyList(),
                restrictions = emptyList()
            )
        )

        // then
        assertEquals(TripFactory.avCabildo4853_virreyDelPino1800_avCabildo20(), result)
        theCommandHasBeenPublished()
    }

    private fun theCommandHasBeenPublished() {
        assertFalse(commandBus.publishedCommands.isEmpty())
    }

    private fun theCommandHasNotBeenPublished() {
        assertTrue(commandBus.publishedCommands.isEmpty())
    }
}