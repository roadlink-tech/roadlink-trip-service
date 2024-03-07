package com.roadlink.tripservice.infrastructure.persistence

import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude.TripLegSolicitude.Status.*
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitudeRepository
import com.roadlink.tripservice.usecases.trip_solicitude.plan.TripPlanApplicationFactory
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

@MicronautTest
class MySQLTripPlanSolicitudeRepositoryTest {

    @Inject
    private lateinit var sectionRepository: SectionRepository

    @Inject
    lateinit var repository: TripPlanSolicitudeRepository

    @Test
    fun `given a trip plan application stored, when find it by passenger id then it must be retrieved`() {
        val tripApplicationId = UUID.randomUUID()
        val passengerId = UUID.randomUUID()
        val tripPlanApplication = TripPlanApplicationFactory.withASingleTripApplication(
            tripApplicationId = tripApplicationId,
            passengerId = passengerId.toString()
        )
        tripPlanApplication.tripLegSolicitudes.flatMap { it.sections }.forEach {
            sectionRepository.save(it)
        }

        repository.insert(tripPlanApplication)

        assertEquals(
            listOf(tripPlanApplication),
            repository.find(TripPlanSolicitudeRepository.CommandQuery(passengerId = passengerId))
        )
    }

    @Test
    fun `given a trip plan application stored, when find it by passenger id and trip application status then it must be retrieved`() {
        val tripApplicationId = UUID.randomUUID()
        val passengerId = UUID.randomUUID()
        val tripPlanApplication = TripPlanApplicationFactory.withASingleTripApplication(
            tripApplicationId = tripApplicationId,
            passengerId = passengerId.toString(),
            tripApplicationStatus = REJECTED
        )
        tripPlanApplication.tripLegSolicitudes.flatMap { it.sections }.forEach {
            sectionRepository.save(it)
        }

        repository.insert(tripPlanApplication)

        assertEquals(
            listOf(tripPlanApplication),
            repository.find(TripPlanSolicitudeRepository.CommandQuery(passengerId = passengerId))
        )
    }

    @Test
    fun `when find trip plan application by passenger id, but there no is anything, then an empty list must be retrieved`() {
        val passengerId = UUID.randomUUID()

        assertEquals(
            listOf<TripPlanSolicitude>(),
            repository.find(TripPlanSolicitudeRepository.CommandQuery(passengerId = passengerId))
        )
    }

    @Test
    fun `given no trip plan application when save one then should be able to find it`() {
        val tripApplicationId = UUID.randomUUID()
        val tripPlanApplication =
            TripPlanApplicationFactory.withASingleTripApplication(tripApplicationId = tripApplicationId)
        tripPlanApplication.tripLegSolicitudes.flatMap { it.sections }.forEach {
            sectionRepository.save(it)
        }

        repository.insert(tripPlanApplication)

        assertEquals(
            tripPlanApplication,
            repository.find(TripPlanSolicitudeRepository.CommandQuery(tripApplicationId = tripApplicationId)).first()
        )
    }

    @Test
    fun `given trip plan application exists and it is modified when update it then should retrieve it`() {
        val passengerId = UUID.randomUUID()
        val tripApplicationId = UUID.randomUUID()
        val tripPlanApplication = givenExists(
            TripPlanApplicationFactory.withASingleTripApplication(
                tripApplicationId = tripApplicationId,
                passengerId = passengerId.toString(),
            )
        )
        tripPlanApplication.confirmApplicationById(
            tripPlanApplication.tripLegSolicitudes.first().id,
            passengerId
        )

        repository.update(tripPlanApplication)

        assertEquals(
            tripPlanApplication,
            repository.find(TripPlanSolicitudeRepository.CommandQuery(tripApplicationId = tripApplicationId)).first()
        )
    }

    @Test
    fun `given no trip plan application exists with the given id when find by trip application id then should return null`() {
        val otherTripApplicationId = UUID.randomUUID()
        givenExists(TripPlanApplicationFactory.withASingleTripApplication())

        val result =
            repository.find(TripPlanSolicitudeRepository.CommandQuery(tripApplicationId = otherTripApplicationId))

        assertTrue(result.isEmpty())
    }


    @Test
    fun `given no trip plan application when save one then should be able to find it by id`() {
        val tripPlanApplication = TripPlanApplicationFactory.withASingleTripApplication()
        tripPlanApplication.tripLegSolicitudes.flatMap { it.sections }.forEach {
            sectionRepository.save(it)
        }

        repository.insert(tripPlanApplication)

        assertEquals(
            tripPlanApplication,
            repository.find(TripPlanSolicitudeRepository.CommandQuery(ids = listOf(tripPlanApplication.id))).first()
        )
    }

    @Test
    fun `given trip plan application exists and it is modified when update it then should retrieve it by id`() {
        val passengerId = UUID.randomUUID()
        val tripPlanApplication = givenExists(
            TripPlanApplicationFactory.withASingleTripApplication(
                passengerId = passengerId.toString(),
            )
        )
        tripPlanApplication.confirmApplicationById(
            tripPlanApplication.tripLegSolicitudes.first().id,
            passengerId
        )

        repository.update(tripPlanApplication)

        assertEquals(
            tripPlanApplication,
            repository.find(TripPlanSolicitudeRepository.CommandQuery(ids = listOf(tripPlanApplication.id))).first()
        )
    }

    @Test
    fun `given no trip plan application exists with the given id when find by id then should return an empty list`() {
        val otherTripPlanApplicationId = UUID.randomUUID()
        givenExists(TripPlanApplicationFactory.withASingleTripApplication())

        val result = repository.find(
            TripPlanSolicitudeRepository.CommandQuery(ids = listOf(otherTripPlanApplicationId))
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