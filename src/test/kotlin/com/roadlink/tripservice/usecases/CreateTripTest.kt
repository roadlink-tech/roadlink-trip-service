package com.roadlink.tripservice.usecases

import com.roadlink.tripservice.SpyEventPublisher
import com.roadlink.tripservice.StubIdGenerator
import com.roadlink.tripservice.StubTimeProvider
import com.roadlink.tripservice.domain.*
import com.roadlink.tripservice.infrastructure.persistence.InMemoryTripRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class CreateTripTest {

    private lateinit var inMemoryTripRepository: InMemoryTripRepository

    private lateinit var stubIdGenerator: StubIdGenerator

    private lateinit var spyEventPublisher: SpyEventPublisher

    private lateinit var stubTimeProvider: StubTimeProvider

    private lateinit var createTrip: CreateTrip

    @BeforeEach
    fun setUp() {
        inMemoryTripRepository = InMemoryTripRepository()
        stubIdGenerator = StubIdGenerator()
        spyEventPublisher = SpyEventPublisher()
        stubTimeProvider = StubTimeProvider(fixedNow = InstantFactory.october15_7hs())

        createTrip = CreateTrip(
            tripRepository = inMemoryTripRepository,
            idGenerator = stubIdGenerator,
            eventPublisher = spyEventPublisher,
            timeProvider = stubTimeProvider,
        )
    }

    @Test
    fun `given already exists trip with same driver in the given time range then should fail`() {
        inMemoryTripRepository.save(TripFactory.avCabildo())

        assertThrows<AlreadyExistsTripByDriverInTimeRange> {
            createTrip(CreateTrip.Request(
                driver = "John Smith",
                vehicle = "Ford mustang",
                departure = TripPointFactory.avCabildo_4853(),
                arrival = TripPointFactory.avCabildo_20(),
                meetingPoints = emptyList(),
                availableSeats = 4,
            ))
        }
        assertEquals(listOf(TripFactory.avCabildo()), inMemoryTripRepository.findAll())
        spyEventPublisher.verifyNoEventHasBeenPublished()
    }

    @Test
    fun `given trip with no meeting points and arrival at before departure at then should fail`() {
        assertThrows<InvalidTripTimeRange> {
            createTrip(CreateTrip.Request(
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
        spyEventPublisher.verifyNoEventHasBeenPublished()
    }

    @Test
    fun `given trip with one meeting point and meeting point at before departure at then should fail`() {
        assertThrows<InvalidTripTimeRange> {
            createTrip(CreateTrip.Request(
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
        spyEventPublisher.verifyNoEventHasBeenPublished()
    }

    @Test
    fun `given trip with one meeting point and meeting point at after arrival at then should fail`() {
        assertThrows<InvalidTripTimeRange> {
            createTrip(CreateTrip.Request(
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
        spyEventPublisher.verifyNoEventHasBeenPublished()
    }

    @Test
    fun `can create trip with no meeting points`() {
        stubIdGenerator.nextIdToGenerate(id = TripFactory.avCabildoId)

        val result = createTrip(CreateTrip.Request(
            driver = "John Smith",
            vehicle = "Ford mustang",
            departure = TripPointFactory.avCabildo_4853(),
            arrival = TripPointFactory.avCabildo_20(),
            meetingPoints = emptyList(),
            availableSeats = 4,
        ))

        assertEquals(TripFactory.avCabildo(), result)
        assertEquals(listOf(TripFactory.avCabildo()), inMemoryTripRepository.findAll())
        spyEventPublisher.verifyHasPublish(TripCreatedEvent(
            trip = TripFactory.avCabildo(),
            at = InstantFactory.october15_7hs(),
        ))
    }

    @Test
    fun `can create trip with meeting points`() {
        stubIdGenerator.nextIdToGenerate(id = TripFactory.avCabildo4853_virreyDelPino1800_avCabildo20)

        val result = createTrip(CreateTrip.Request(
            driver = "John Smith",
            vehicle = "Ford mustang",
            departure = TripPointFactory.avCabildo_4853(),
            arrival = TripPointFactory.avCabildo_20(),
            meetingPoints = listOf(TripPointFactory.virreyDelPino_1800()),
            availableSeats = 5,
        ))

        assertEquals(TripFactory.avCabildo4853_virreyDelPino1800_avCabildo20(), result)
        assertEquals(listOf(TripFactory.avCabildo4853_virreyDelPino1800_avCabildo20()), inMemoryTripRepository.findAll())
        spyEventPublisher.verifyHasPublish(TripCreatedEvent(
            trip = TripFactory.avCabildo4853_virreyDelPino1800_avCabildo20(),
            at = InstantFactory.october15_7hs(),
        ))
    }
}