package com.roadlink.tripservice.config.trip

import com.roadlink.tripservice.domain.trip.TripRepository
import com.roadlink.tripservice.infrastructure.persistence.trip.MySQLTripRepository
import io.micronaut.context.annotation.Factory
import io.micronaut.transaction.TransactionOperations
import jakarta.inject.Singleton
import jakarta.persistence.EntityManager
import org.hibernate.Session

@Factory
class TripRepositoryConfig {
    @Singleton
    fun tripRepository(entityManager: EntityManager, transactionManager: TransactionOperations<Session>): TripRepository {
        return MySQLTripRepository(entityManager = entityManager, transactionManager = transactionManager)
    }
}