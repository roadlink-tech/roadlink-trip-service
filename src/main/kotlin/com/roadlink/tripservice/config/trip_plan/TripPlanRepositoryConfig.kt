package com.roadlink.tripservice.config.trip_plan

import com.roadlink.tripservice.domain.trip_plan.TripPlanRepository
import com.roadlink.tripservice.infrastructure.persistence.trip_plan.MySQLTripPlanRepository
import io.micronaut.context.annotation.Factory
import io.micronaut.transaction.TransactionOperations
import jakarta.inject.Singleton
import jakarta.persistence.EntityManager
import org.hibernate.Session

@Factory
class TripPlanRepositoryConfig {

    @Singleton
    fun tripPlanRepository(
        entityManager: EntityManager,
        transactionManager: TransactionOperations<Session>
    ): TripPlanRepository {
        return MySQLTripPlanRepository(entityManager, transactionManager)
    }
}