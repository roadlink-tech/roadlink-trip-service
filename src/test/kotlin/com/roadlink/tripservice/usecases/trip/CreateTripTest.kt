package com.roadlink.tripservice.usecases.trip

import com.roadlink.tripservice.domain.trip.exception.AlreadyExistsTripByDriverInTimeRange
import com.roadlink.tripservice.domain.common.utils.time.exception.InvalidTripTimeRange
import com.roadlink.tripservice.infrastructure.persistence.trip.InMemoryTripRepository
import com.roadlink.tripservice.config.SpyCommandBus
import com.roadlink.tripservice.config.StubCreateTripHandler
import com.roadlink.tripservice.config.StubIdGenerator
import com.roadlink.tripservice.config.StubTimeProvider
import com.roadlink.tripservice.usecases.common.TripPointFactory
import com.roadlink.tripservice.usecases.factory.InstantFactory
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class CreateTripTest {

    private lateinit var inMemoryTripRepository: InMemoryTripRepository

    private lateinit var stubIdGenerator: StubIdGenerator

    private lateinit var stubTimeProvider: StubTimeProvider

    private lateinit var commandBus: SpyCommandBus

    private lateinit var createTrip: CreateTrip

    @BeforeEach
    fun setUp() {
        inMemoryTripRepository = InMemoryTripRepository()
        stubIdGenerator = StubIdGenerator()
        stubTimeProvider = StubTimeProvider(fixedNow = InstantFactory.october15_7hs())
        commandBus = SpyCommandBus()
        commandBus.registerHandler(StubCreateTripHandler())

        createTrip = CreateTrip(
            tripRepository = inMemoryTripRepository,
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
        inMemoryTripRepository.save(TripFactory.avCabildo4853_to_avCabildo20())

        assertThrows<AlreadyExistsTripByDriverInTimeRange> {
            createTrip(
                CreateTrip.Input(
                driver = "John Smith",
                vehicle = "Ford mustang",
                departure = TripPointFactory.avCabildo_4853(),
                arrival = TripPointFactory.avCabildo_20(),
                meetingPoints = emptyList(),
                availableSeats = 4,
            ))
        }
        assertEquals(listOf(TripFactory.avCabildo4853_to_avCabildo20()), inMemoryTripRepository.findAll())
        theCommandHasNotBeenPublished()
    }

    @Test
    fun `given trip with no meeting points and arrival at before departure at then should fail`() {
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
            ))
        }
        assertTrue(inMemoryTripRepository.isEmpty())
        theCommandHasNotBeenPublished()
    }

    @Test
    fun `given trip with one meeting point and meeting point at before departureAt then should fail`() {
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
            ))
        }
        assertTrue(inMemoryTripRepository.isEmpty())
        theCommandHasNotBeenPublished()
    }

    @Test
    fun `given trip with one meeting point and meeting point at after arrival at then should fail`() {
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
            ))
        }
        assertTrue(inMemoryTripRepository.isEmpty())
        theCommandHasNotBeenPublished()
    }

    @Test
    fun `can create trip with no meeting points`() {
        stubIdGenerator.nextIdToGenerate(id = TripFactory.avCabildo_id)

        val result = createTrip(
            CreateTrip.Input(
            driver = "John Smith",
            vehicle = "Ford mustang",
            departure = TripPointFactory.avCabildo_4853(),
            arrival = TripPointFactory.avCabildo_20(),
            meetingPoints = emptyList(),
            availableSeats = 4,
        ))

        assertEquals(TripFactory.avCabildo4853_to_avCabildo20(), result)
        assertEquals(listOf(TripFactory.avCabildo4853_to_avCabildo20()), inMemoryTripRepository.findAll())
        theCommandHasBeenPublished()
    }

    @Test
    fun `can create trip with meeting points`() {
        stubIdGenerator.nextIdToGenerate(id = TripFactory.avCabildo4853_virreyDelPino1800_avCabildo20_id)

        val result = createTrip(
            CreateTrip.Input(
            driver = "John Smith",
            vehicle = "Ford mustang",
            departure = TripPointFactory.avCabildo_4853(),
            arrival = TripPointFactory.avCabildo_20(),
            meetingPoints = listOf(TripPointFactory.virreyDelPino_1800()),
            availableSeats = 5,
        ))

        assertEquals(TripFactory.avCabildo4853_virreyDelPino1800_avCabildo20(), result)
        assertEquals(
            listOf(TripFactory.avCabildo4853_virreyDelPino1800_avCabildo20()),
            inMemoryTripRepository.findAll()
        )
        theCommandHasBeenPublished()
    }

    private fun theCommandHasBeenPublished() {
        assertFalse(commandBus.publishedCommands.isEmpty())
    }

    private fun theCommandHasNotBeenPublished() {
        assertTrue(commandBus.publishedCommands.isEmpty())
    }
}