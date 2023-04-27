package com.roadlink.tripservice.trip.usecases

import com.roadlink.tripservice.domain.trip.AlreadyRequestedJoinTrip
import com.roadlink.tripservice.infrastructure.persistence.InMemoryJoinTripRequestRepository
import com.roadlink.tripservice.trip.StubIdGenerator
import com.roadlink.tripservice.trip.domain.JoinTripRequestFactory
import com.roadlink.tripservice.trip.domain.SectionFactory
import com.roadlink.tripservice.usecases.JoinTripRequest
import com.roadlink.tripservice.usecases.RequestJoinTrip
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class RequestJoinTripTest {

    private lateinit var result: Set<JoinTripRequest>

    private lateinit var stubIdGenerator: StubIdGenerator
    private lateinit var inMemoryJoinTripRequestRepository: InMemoryJoinTripRequestRepository

    private lateinit var requestJoinTrip: RequestJoinTrip

    @BeforeEach
    fun setUp() {
        stubIdGenerator = StubIdGenerator()
        inMemoryJoinTripRequestRepository = InMemoryJoinTripRequestRepository()

        requestJoinTrip = RequestJoinTrip(
            idGenerator = stubIdGenerator,
            joinTripRequestRepository = inMemoryJoinTripRequestRepository,
        )
    }

    @Test
    fun `can request join to a section`() {
        stubIdGenerator.nextIdToGenerate(JoinTripRequestFactory.avCabildo_id)

        result = requestJoinTrip(RequestJoinTrip.Input(
            passengerId = "passenger-id",
            sectionIds = setOf(SectionFactory.avCabildo_id),
        ))

        thenResultEquals(JoinTripRequestFactory.avCabildo())
        thenJoinTripRequestExists(JoinTripRequestFactory.avCabildo())
    }

    @Test
    fun `can request join to multiple sections`() {
        stubIdGenerator.nextIdToGenerate(
            JoinTripRequestFactory.avCabildo4853_virreyDelPino1800_id,
            JoinTripRequestFactory.virreyDelPino1800_avCabildo20_id,
        )

        result = requestJoinTrip(RequestJoinTrip.Input(
            passengerId = "passenger-id",
            sectionIds = setOf(
                SectionFactory.avCabildo4853_virreyDelPino1800_id,
                SectionFactory.virreyDelPino1800_avCabildo20_id,
            ),
        ))

        thenResultEquals(
            JoinTripRequestFactory.avCabildo4853_virreyDelPino1800(),
            JoinTripRequestFactory.virreyDelPino1800_avCabildo20()
        )
        thenJoinTripRequestExists(
            JoinTripRequestFactory.avCabildo4853_virreyDelPino1800(),
            JoinTripRequestFactory.virreyDelPino1800_avCabildo20()
        )
    }

    @Test
    fun `given passenger already send request to join section then should fail`() {
        givenAlreadyExists(JoinTripRequestFactory.avCabildo())

        assertThrows<AlreadyRequestedJoinTrip> {
            requestJoinTrip(RequestJoinTrip.Input(
                passengerId = "passenger-id",
                sectionIds = setOf(SectionFactory.avCabildo_id),
            ))
        }

        thenJoinTripRequestExists(JoinTripRequestFactory.avCabildo())
    }

    @Test
    fun `given passenger already send request to join one of the sections then should fail`() {
        givenAlreadyExists(JoinTripRequestFactory.virreyDelPino1800_avCabildo20())
        stubIdGenerator.nextIdToGenerate(
            JoinTripRequestFactory.avCabildo4853_virreyDelPino1800_id,
            JoinTripRequestFactory.virreyDelPino1800_avCabildo20_id,
        )

        assertThrows<AlreadyRequestedJoinTrip> {
            requestJoinTrip(RequestJoinTrip.Input(
                passengerId = "passenger-id",
                sectionIds = setOf(
                    SectionFactory.avCabildo4853_virreyDelPino1800_id,
                    SectionFactory.virreyDelPino1800_avCabildo20_id,
                ),
            ))
        }
        thenJoinTripRequestExists(JoinTripRequestFactory.virreyDelPino1800_avCabildo20())
    }

    @Test
    fun `given section has no available seats then should fail`() {
        // solo cuentan cuando estan aceptadas las request
    }

    @Test
    fun `given passenger already send request to join trip in same time range then should fail`() {

    }

    @Test
    fun `request join to a trip that not exists`() {
    }

    private fun givenAlreadyExists(joinTripRequest: JoinTripRequest) {
        inMemoryJoinTripRequestRepository.save(setOf(joinTripRequest))
    }

    private fun givenAlreadyExists(vararg joinTripRequest: JoinTripRequest) {
        inMemoryJoinTripRequestRepository.save(joinTripRequest.toSet())
    }

    private fun thenResultEquals(joinTripRequest: JoinTripRequest) {
        assertEquals(setOf(joinTripRequest), result)
    }

    private fun thenResultEquals(vararg joinTripRequest: JoinTripRequest) {
        assertEquals(joinTripRequest.toSet(), result)
    }

    private fun thenJoinTripRequestExists(joinTripRequest: JoinTripRequest) {
        assertEquals(listOf(joinTripRequest), inMemoryJoinTripRequestRepository.findAll())
    }

    private fun thenJoinTripRequestExists(vararg joinTripRequest: JoinTripRequest) {
        assertEquals(joinTripRequest.toList(), inMemoryJoinTripRequestRepository.findAll())
    }
}

/*
si un conductor deshabilito una de las solicitudes las demas se deben rechazar tambien

 */