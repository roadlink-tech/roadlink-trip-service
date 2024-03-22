package com.roadlink.tripservice.infrastructure.persistence.trip_solicitude

import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude.TripLegSolicitude.Status.*
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitudeRepository
import com.roadlink.tripservice.usecases.trip_solicitude.plan.TripPlanSolicitudeFactory
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

@MicronautTest
class MySQLTripPlanSolicitudeRepositoryTestResult {

    @Inject
    private lateinit var sectionRepository: SectionRepository

    @Inject
    lateinit var repository: TripPlanSolicitudeRepository

    @Test
    fun `given a trip plan solicitude stored, when find it by passenger id then it must be retrieved`() {
        val tripApplicationId = UUID.randomUUID()
        val passengerId = UUID.randomUUID()
        val tripPlanSolicitude = TripPlanSolicitudeFactory.withASingleTripApplication(
            tripApplicationId = tripApplicationId,
            passengerId = passengerId.toString()
        )
        tripPlanSolicitude.tripLegSolicitudes.flatMap { it.sections }.forEach {
            sectionRepository.save(it)
        }

        repository.insert(tripPlanSolicitude)

        assertEquals(
            listOf(tripPlanSolicitude),
            repository.find(TripPlanSolicitudeRepository.CommandQuery(passengerId = passengerId))
        )
    }

    @Test
    fun `given a trip plan solicitude stored, when find it by passenger id and trip application status then it must be retrieved`() {
        val tripApplicationId = UUID.randomUUID()
        val passengerId = UUID.randomUUID()
        val tripPlanSolicitude = TripPlanSolicitudeFactory.withASingleTripApplication(
            tripApplicationId = tripApplicationId,
            passengerId = passengerId.toString(),
            tripApplicationStatus = REJECTED
        )
        tripPlanSolicitude.tripLegSolicitudes.flatMap { it.sections }.forEach {
            sectionRepository.save(it)
        }

        repository.insert(tripPlanSolicitude)

        assertEquals(
            listOf(tripPlanSolicitude),
            repository.find(TripPlanSolicitudeRepository.CommandQuery(passengerId = passengerId))
        )
    }

    @Test
    fun `when find trip plan solicitude by passenger id, but there no is anything, then an empty list must be retrieved`() {
        val passengerId = UUID.randomUUID()

        assertEquals(
            listOf<TripPlanSolicitude>(),
            repository.find(TripPlanSolicitudeRepository.CommandQuery(passengerId = passengerId))
        )
    }

    @Test
    fun `given no trip plan solicitude when save one then should be able to find it`() {
        val tripApplicationId = UUID.randomUUID()
        val tripPlanSolicitude =
            TripPlanSolicitudeFactory.withASingleTripApplication(tripApplicationId = tripApplicationId)
        tripPlanSolicitude.tripLegSolicitudes.flatMap { it.sections }.forEach {
            sectionRepository.save(it)
        }

        repository.insert(tripPlanSolicitude)

        assertEquals(
            tripPlanSolicitude,
            repository.find(TripPlanSolicitudeRepository.CommandQuery(tripLegSolicitudeId = tripApplicationId)).first()
        )
    }

    @Test
    fun `given trip plan solicitude exists and it is modified when update it then should retrieve it`() {
        val passengerId = UUID.randomUUID()
        val tripApplicationId = UUID.randomUUID()
        val tripPlanSolicitude = givenExists(
            TripPlanSolicitudeFactory.withASingleTripApplication(
                tripApplicationId = tripApplicationId,
                passengerId = passengerId.toString(),
            )
        )
        tripPlanSolicitude.acceptTripLegSolicitude(
            tripPlanSolicitude.tripLegSolicitudes.first().id,
            passengerId
        )

        repository.update(tripPlanSolicitude)

        assertEquals(
            tripPlanSolicitude,
            repository.find(TripPlanSolicitudeRepository.CommandQuery(tripLegSolicitudeId = tripApplicationId)).first()
        )
    }

    @Test
    fun `given no trip plan solicitude exists with the given id when find by trip application id then should return null`() {
        val otherTripApplicationId = UUID.randomUUID()
        givenExists(TripPlanSolicitudeFactory.withASingleTripApplication())

        val result =
            repository.find(TripPlanSolicitudeRepository.CommandQuery(tripLegSolicitudeId = otherTripApplicationId))

        assertTrue(result.isEmpty())
    }


    @Test
    fun `given no trip plan solicitude when save one then should be able to find it by id`() {
        val tripPlanSolicitude = TripPlanSolicitudeFactory.withASingleTripApplication()
        tripPlanSolicitude.tripLegSolicitudes.flatMap { it.sections }.forEach {
            sectionRepository.save(it)
        }

        repository.insert(tripPlanSolicitude)

        assertEquals(
            tripPlanSolicitude,
            repository.find(TripPlanSolicitudeRepository.CommandQuery(ids = listOf(tripPlanSolicitude.id))).first()
        )
    }

    @Test
    fun `given trip plan solicitude exists and it is modified when update it then should retrieve it by id`() {
        val passengerId = UUID.randomUUID()
        val tripPlanSolicitude = givenExists(
            TripPlanSolicitudeFactory.withASingleTripApplication(
                passengerId = passengerId.toString(),
            )
        )
        tripPlanSolicitude.acceptTripLegSolicitude(
            tripPlanSolicitude.tripLegSolicitudes.first().id,
            passengerId
        )

        repository.update(tripPlanSolicitude)

        assertEquals(
            tripPlanSolicitude,
            repository.find(TripPlanSolicitudeRepository.CommandQuery(ids = listOf(tripPlanSolicitude.id))).first()
        )
    }

    @Test
    fun `given no trip plan solicitude exists with the given id when find by id then should return an empty list`() {
        val otherTripPlanSolicitudeId = UUID.randomUUID()
        givenExists(TripPlanSolicitudeFactory.withASingleTripApplication())

        val result = repository.find(
            TripPlanSolicitudeRepository.CommandQuery(ids = listOf(otherTripPlanSolicitudeId))
        )

        assertTrue(result.isEmpty())
    }

    private fun givenExists(tripPlanSolicitude: TripPlanSolicitude): TripPlanSolicitude {
        tripPlanSolicitude.tripLegSolicitudes.flatMap { it.sections }.forEach {
            sectionRepository.save(it)
        }

        repository.insert(tripPlanSolicitude)

        return tripPlanSolicitude
    }
}