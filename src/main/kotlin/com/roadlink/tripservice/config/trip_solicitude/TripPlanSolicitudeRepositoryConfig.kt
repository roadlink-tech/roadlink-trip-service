package com.roadlink.tripservice.config.trip_solicitude

import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitudeRepository
import com.roadlink.tripservice.infrastructure.persistence.trip_application.plan.MySQLTripPlanSolicitudeRepository
import com.roadlink.tripservice.usecases.UseCase
import com.roadlink.tripservice.usecases.trip_solicitude.plan.ListTripPlanSolicitudes
import io.micronaut.context.annotation.Factory
import io.micronaut.transaction.TransactionOperations
import jakarta.inject.Singleton
import jakarta.persistence.EntityManager
import org.hibernate.Session

@Factory
class TripPlanSolicitudeRepositoryConfig {
    @Singleton
    fun tripPlanSolicitudeRepository(
        entityManager: EntityManager,
        transactionManager: TransactionOperations<Session>
    ): TripPlanSolicitudeRepository {
        return MySQLTripPlanSolicitudeRepository(
            entityManager = entityManager,
            transactionManager = transactionManager
        )
    }

    @Singleton
    fun listTripPlanSolicitudes(
        tripPlanSolicitudeRepository: TripPlanSolicitudeRepository
    ): UseCase<ListTripPlanSolicitudes.Input, ListTripPlanSolicitudes.Output> {
        return ListTripPlanSolicitudes(tripPlanSolicitudeRepository)
    }

}