package com.roadlink.tripservice.config

import com.roadlink.tripservice.infrastructure.persistence.MySQLTripRepository
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton
import jakarta.persistence.EntityManager

@Factory
class MySQLTripRepositoryConfig {
    @Singleton
    fun mySQLTripRepository(entityManager: EntityManager): MySQLTripRepository {
        return MySQLTripRepository(entityManager)
    }
}