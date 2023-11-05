package com.roadlink.tripservice.trip.infrastructure.persistence

import com.roadlink.tripservice.infrastructure.persistence.trip_application.MySQLTripPlanApplicationRepository
import com.roadlink.tripservice.trip.domain.SectionFactory
import com.roadlink.tripservice.trip.domain.TripApplicationFactory
import com.roadlink.tripservice.trip.domain.TripPlanApplicationFactory
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

@MicronautTest
class MySQLTripPlanApplicationRepositoryTest {

    @Inject
    lateinit var repository: MySQLTripPlanApplicationRepository

    @Test
    fun `given no trip plan application when save one then should be able to find it`() {
        val tripPlanApplication = TripPlanApplicationFactory.withASingleTripApplication()

        repository.insert(tripPlanApplication)

        assertEquals(
            tripPlanApplication,
            repository.findByTripApplicationId(tripPlanApplication.id)
        )
    }

    @Test
    fun `given trip plan application exists and it is modified when update it then should retrieve it`() {
        val passengerId = UUID.randomUUID()
        val tripPlanApplication = TripPlanApplicationFactory.withASingleTripApplication(
            passengerId = passengerId.toString(),
        )
        repository.insert(tripPlanApplication)
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
        val tripPlanApplication = TripPlanApplicationFactory.withASingleTripApplication()
        repository.insert(tripPlanApplication)

        val result = repository.findByTripApplicationId(otherTripPlanApplicationId)

        assertNull(result)
    }

    @Test
    fun `given trip application has the given section when find by section then should return empty it`() {
        val avCabildoSection = SectionFactory.avCabildo()
        val tripApplication = TripApplicationFactory.withSections(listOf(avCabildoSection))
        val tripPlanApplication = TripPlanApplicationFactory.withApplications(listOf(tripApplication))
        repository.insert(tripPlanApplication)

        val result = repository.findTripApplicationBySectionId(avCabildoSection.id)

        assertEquals(setOf(tripApplication), result)
    }

    @Test
    fun `given no trip application has the given section when find by section then should return empty set`() {
        val avCabildoSection = SectionFactory.avCabildo()
        val tripApplication = TripApplicationFactory.withSections(listOf(avCabildoSection))
        val tripPlanApplication = TripPlanApplicationFactory.withApplications(listOf(tripApplication))
        repository.insert(tripPlanApplication)

        val result = repository.findTripApplicationBySectionId(SectionFactory.virreyDelPino_id)

        assertTrue { result.isEmpty() }
    }

}