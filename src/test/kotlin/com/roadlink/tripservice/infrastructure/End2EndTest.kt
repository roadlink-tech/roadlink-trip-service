package com.roadlink.tripservice.infrastructure

import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.BeforeEach

abstract class End2EndTest {

    @Inject
    lateinit var entityManager: EntityManager

    @BeforeEach
    fun setUp() {
        deleteAllFromDatabase()
    }

    private fun deleteAllFromDatabase() {
        // Please do not change the entity deletion sort, because those are taking into account the referencial integrity.
        // Also consider to implement a CASCADE deletion strategy
        setOf(
            "TripJPAEntity", "TripLegSolicitudeJPAEntity", "TripPlanSolicitudeJPAEntity", "SectionJPAEntity"
        ).forEach {
            entityManager.createQuery("DELETE FROM $it")
                .executeUpdate()
        }
    }
}