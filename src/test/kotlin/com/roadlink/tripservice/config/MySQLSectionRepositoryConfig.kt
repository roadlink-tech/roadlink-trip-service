package com.roadlink.tripservice.config

import com.roadlink.tripservice.infrastructure.persistence.MySQLSectionRepository
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton
import jakarta.persistence.EntityManager

@Factory
class MySQLSectionRepositoryConfig {
    @Singleton
    fun mySQLSectionRepository(entityManager: EntityManager): MySQLSectionRepository {
        return MySQLSectionRepository(entityManager)
    }
}