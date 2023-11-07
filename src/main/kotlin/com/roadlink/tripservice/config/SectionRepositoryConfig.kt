package com.roadlink.tripservice.config

import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.infrastructure.persistence.MySQLSectionRepository
import io.micronaut.context.annotation.Factory
import io.micronaut.transaction.TransactionOperations
import jakarta.inject.Singleton
import jakarta.persistence.EntityManager
import org.hibernate.Session

@Factory
class SectionRepositoryConfig {
    @Singleton
    fun sectionRepository(entityManager: EntityManager, transactionManager: TransactionOperations<Session>): SectionRepository {
        return MySQLSectionRepository(entityManager = entityManager, transactionManager = transactionManager)
    }
}