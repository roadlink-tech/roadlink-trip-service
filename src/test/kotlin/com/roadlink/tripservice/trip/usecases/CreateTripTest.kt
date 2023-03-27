package com.roadlink.tripservice.trip.usecases

import com.roadlink.tripservice.domain.time.exception.InvalidTripTimeRangeException
import com.roadlink.tripservice.infrastructure.persistence.InMemoryTripRepository
import com.roadlink.tripservice.trip.*
import com.roadlink.tripservice.trip.domain.InstantFactory
import com.roadlink.tripservice.trip.domain.TripFactory
import com.roadlink.tripservice.trip.domain.TripPointFactory
import com.roadlink.tripservice.usecases.CreateTrip
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows


internal class CreateTripTest {

    private lateinit var inMemoryTripRepository: InMemoryTripRepository

    private lateinit var stubIdGenerator: StubIdGenerator

    private lateinit var spyEventPublisher: SpyEventPublisher

    private lateinit var stubTimeProvider: StubTimeProvider

    private lateinit var commandBus: SpyCommandBus

    private lateinit var createTrip: CreateTrip

    @BeforeEach
    fun setUp() {
        inMemoryTripRepository = InMemoryTripRepository()
        stubIdGenerator = StubIdGenerator()
        spyEventPublisher = SpyEventPublisher()
        stubTimeProvider = StubTimeProvider(fixedNow = InstantFactory.october15_7hs())
        commandBus = SpyCommandBus()
        commandBus.registerHandler(SpyCreateTripHandler())

        createTrip = CreateTrip(
            tripRepository = inMemoryTripRepository,
            idGenerator = stubIdGenerator,
            eventPublisher = spyEventPublisher,
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
        inMemoryTripRepository.save(TripFactory.avCabildo())

        assertThrows<com.roadlink.tripservice.domain.AlreadyExistsTripByDriverInTimeRange> {
            createTrip(
                CreateTrip.Request(
                    driver = "John Smith",
                    vehicle = "Ford mustang",
                    departure = TripPointFactory.avCabildo_4853(),
                    arrival = TripPointFactory.avCabildo_20(),
                    meetingPoints = emptyList(),
                    availableSeats = 4,
                )
            )
        }
        assertEquals(listOf(TripFactory.avCabildo()), inMemoryTripRepository.findAll())
        theCommandHasNotBeenPublished()
    }

    @Test
    fun `given trip with no meeting points and arrival at before departure at then should fail`() {
        assertThrows<InvalidTripTimeRangeException> {
            createTrip(
                CreateTrip.Request(
                    driver = "John Smith",
                    vehicle = "Ford mustang",
                    departure = TripPointFactory.avCabildo_4853(),
                    arrival = TripPointFactory.avCabildo_20(
                        at = InstantFactory.october15_7hs(),
                    ),
                    meetingPoints = emptyList(),
                    availableSeats = 4,
                )
            )
        }
        assertTrue(inMemoryTripRepository.isEmpty())
        theCommandHasNotBeenPublished()
    }

    @Test
    fun `given trip with one meeting point and meeting point at before departureAt then should fail`() {
        assertThrows<InvalidTripTimeRangeException> {
            createTrip(
                CreateTrip.Request(
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
                )
            )
        }
        assertTrue(inMemoryTripRepository.isEmpty())
        theCommandHasNotBeenPublished()
    }

    @Test
    fun `given trip with one meeting point and meeting point at after arrival at then should fail`() {
        assertThrows<InvalidTripTimeRangeException> {
            createTrip(
                CreateTrip.Request(
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
                )
            )
        }
        assertTrue(inMemoryTripRepository.isEmpty())
        theCommandHasNotBeenPublished()
    }

    @Test
    fun `can create trip with no meeting points`() {
        stubIdGenerator.nextIdToGenerate(id = TripFactory.avCabildoId)

        val result = createTrip(
            CreateTrip.Request(
                driver = "John Smith",
                vehicle = "Ford mustang",
                departure = TripPointFactory.avCabildo_4853(),
                arrival = TripPointFactory.avCabildo_20(),
                meetingPoints = emptyList(),
                availableSeats = 4,
            )
        )

        assertEquals(TripFactory.avCabildo(), result)
        assertEquals(listOf(TripFactory.avCabildo()), inMemoryTripRepository.findAll())
        theCommandHasBeenPublished()
    }

    @Test
    fun `can create trip with meeting points`() {
        stubIdGenerator.nextIdToGenerate(id = TripFactory.avCabildo4853_virreyDelPino1800_avCabildo20)

        val result = createTrip(
            CreateTrip.Request(
                driver = "John Smith",
                vehicle = "Ford mustang",
                departure = TripPointFactory.avCabildo_4853(),
                arrival = TripPointFactory.avCabildo_20(),
                meetingPoints = listOf(TripPointFactory.virreyDelPino_1800()),
                availableSeats = 5,
            )
        )

        assertEquals(TripFactory.avCabildo4853_virreyDelPino1800_avCabildo20(), result)
        assertEquals(
            listOf(TripFactory.avCabildo4853_virreyDelPino1800_avCabildo20()),
            inMemoryTripRepository.findAll()
        )
        theCommandHasBeenPublished()
    }

    private fun theCommandHasBeenPublished() {
        Assertions.assertFalse(commandBus.publishedCommands.isEmpty())
    }

    private fun theCommandHasNotBeenPublished() {
        Assertions.assertTrue(commandBus.publishedCommands.isEmpty())
    }
}