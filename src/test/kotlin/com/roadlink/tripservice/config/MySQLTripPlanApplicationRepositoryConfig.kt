package com.roadlink.tripservice.config

import com.roadlink.tripservice.infrastructure.persistence.trip_application.MySQLTripApplicationRepository
import com.roadlink.tripservice.infrastructure.persistence.trip_application.MySQLTripPlanApplicationRepository
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton
import jakarta.persistence.EntityManager

@Factory
class MySQLTripPlanApplicationRepositoryConfig {
    @Singleton
    fun mySQLTripPlanApplicationRepository(
        entityManager: EntityManager,
    ): MySQLTripPlanApplicationRepository {
        return MySQLTripPlanApplicationRepository(entityManager)
    }
}

@Factory
class MySQLTripApplicationRepositoryConfig {
    @Singleton
    fun mySQLTripApplicationRepository(entityManager: EntityManager): MySQLTripApplicationRepository {
        return MySQLTripApplicationRepository(entityManager)
    }
}
