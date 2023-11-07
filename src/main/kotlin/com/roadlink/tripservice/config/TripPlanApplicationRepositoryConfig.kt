package com.roadlink.tripservice.config

import com.roadlink.tripservice.domain.trip_application.TripPlanApplicationRepository
import com.roadlink.tripservice.infrastructure.persistence.trip_application.MySQLTripPlanApplicationRepository
import io.micronaut.context.annotation.Factory
import io.micronaut.transaction.TransactionOperations
import jakarta.inject.Singleton
import jakarta.persistence.EntityManager
import org.hibernate.Session

@Factory
class TripPlanApplicationRepositoryConfig {
    @Singleton
    fun tripPlanApplicationRepository(entityManager: EntityManager, transactionManager: TransactionOperations<Session>): TripPlanApplicationRepository {
        return MySQLTripPlanApplicationRepository(entityManager = entityManager, transactionManager = transactionManager)
    }
}