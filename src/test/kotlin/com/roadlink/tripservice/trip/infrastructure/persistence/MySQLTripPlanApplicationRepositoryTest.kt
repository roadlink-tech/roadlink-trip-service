package com.roadlink.tripservice.trip.infrastructure.persistence

import com.roadlink.tripservice.infrastructure.persistence.MySQLSectionRepository
import com.roadlink.tripservice.infrastructure.persistence.trip_application.MySQLTripPlanApplicationRepository
import com.roadlink.tripservice.trip.domain.TripPlanApplicationFactory
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@MicronautTest
class MySQLTripPlanApplicationRepositoryTest {

    @Inject
    lateinit var sectionRepository: MySQLSectionRepository

    @Inject
    lateinit var repository: MySQLTripPlanApplicationRepository

    @Test
    fun `given no trip plan application when save one then should be able to find it`() {
        val tripPlanApplication = TripPlanApplicationFactory.completed()
        sectionRepository.saveAll(tripPlanApplication.tripApplications.flatMap { it.sections }.toSet())

        repository.save(tripPlanApplication)

        assertEquals(
            tripPlanApplication,
            repository.findByTripApplicationId(tripPlanApplication.id)
        )
    }


}