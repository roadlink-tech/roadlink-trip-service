package com.roadlink.tripservice.infrastructure.persistence

import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.domain.trip_application.TripPlanApplication
import com.roadlink.tripservice.domain.trip_application.TripPlanApplicationRepository
import com.roadlink.tripservice.usecases.trip.domain.SectionFactory
import com.roadlink.tripservice.usecases.trip.domain.TripApplicationFactory
import com.roadlink.tripservice.usecases.trip.domain.TripPlanApplicationFactory
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

@MicronautTest
class MySQLTripPlanApplicationRepositoryTest {

    @Inject
    private lateinit var sectionRepository: SectionRepository

    @Inject
    lateinit var repository: TripPlanApplicationRepository

    @Test
    fun `given no trip plan application when save one then should be able to find it`() {
        val tripPlanApplication = TripPlanApplicationFactory.withASingleTripApplication()
        tripPlanApplication.tripApplications.flatMap { it.sections }.forEach {
            sectionRepository.save(it)
        }

        repository.insert(tripPlanApplication)

        assertEquals(
            tripPlanApplication,
            repository.findByTripApplicationId(tripPlanApplication.id)
        )
    }

    @Test
    fun `given trip plan application exists and it is modified when update it then should retrieve it`() {
        val passengerId = UUID.randomUUID()
        val tripPlanApplication = givenExists(
            TripPlanApplicationFactory.withASingleTripApplication(
            passengerId = passengerId.toString(),
        ))
        tripPlanApplication.confirmApplicationById(
            tripPlanApplication.tripApplications.first().id,
            passengerId
        )

        repository.update(tripPlanApplication)

        assertEquals(
            tripPlanApplication,
            repository.findByTripApplicationId(tripPlanApplication.id)
        )
    }

    @Test
    fun `given no trip plan application exists with the given id when find by id then should return null`() {
        val otherTripPlanApplicationId = UUID.randomUUID()
        givenExists(TripPlanApplicationFactory.withASingleTripApplication())

        val result = repository.findByTripApplicationId(otherTripPlanApplicationId)

        assertNull(result)
    }

    @Test
    fun `given trip application has the given section when find by section then should return empty it`() {
        val avCabildoSection = SectionFactory.avCabildo()
        val tripApplication = TripApplicationFactory.withSections(listOf(avCabildoSection))
        givenExists(TripPlanApplicationFactory.withApplications(listOf(tripApplication)))

        val result = repository.findTripApplicationBySectionId(avCabildoSection.id)

        assertEquals(setOf(tripApplication), result)
    }

    @Test
    fun `given no trip application has the given section when find by section then should return empty set`() {
        val avCabildoSection = SectionFactory.avCabildo()
        val tripApplication = TripApplicationFactory.withSections(listOf(avCabildoSection))
        givenExists(TripPlanApplicationFactory.withApplications(listOf(tripApplication)))

        val result = repository.findTripApplicationBySectionId(SectionFactory.virreyDelPino_id)

        assertTrue { result.isEmpty() }
    }

    private fun givenExists(tripPlanApplication: TripPlanApplication): TripPlanApplication {
        tripPlanApplication.tripApplications.flatMap { it.sections }.forEach {
            sectionRepository.save(it)
        }

        repository.insert(tripPlanApplication)

        return tripPlanApplication
    }
}