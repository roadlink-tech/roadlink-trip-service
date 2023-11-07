package com.roadlink.tripservice.trip.infrastructure.rest

import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.BeforeEach

abstract class End2EndTest {

    @Inject
    private lateinit var entityManager: EntityManager

    @BeforeEach
    fun setUp() {
        deleteAllFromDatabase()
    }

    private fun deleteAllFromDatabase() {
        setOf("TripJPAEntity", "SectionJPAEntity", "TripPlanApplicationJPAEntity",
            "TripApplicationJPAEntity"
        ).forEach {
                entityManager.createQuery("DELETE FROM $it")
                    .executeUpdate()
            }
    }
}