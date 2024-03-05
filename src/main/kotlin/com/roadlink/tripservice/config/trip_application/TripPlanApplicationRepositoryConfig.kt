package com.roadlink.tripservice.config.trip_application

import com.roadlink.tripservice.domain.trip_application.TripPlanApplicationRepository
import com.roadlink.tripservice.infrastructure.persistence.trip_application.plan.MySQLTripPlanApplicationRepository
import com.roadlink.tripservice.usecases.UseCase
import com.roadlink.tripservice.usecases.trip_application.plan.ListTripPlanApplications
import io.micronaut.context.annotation.Factory
import io.micronaut.transaction.TransactionOperations
import jakarta.inject.Singleton
import jakarta.persistence.EntityManager
import org.hibernate.Session

@Factory
class TripPlanApplicationRepositoryConfig {
    @Singleton
    fun tripPlanApplicationRepository(
        entityManager: EntityManager,
        transactionManager: TransactionOperations<Session>
    ): TripPlanApplicationRepository {
        return MySQLTripPlanApplicationRepository(
            entityManager = entityManager,
            transactionManager = transactionManager
        )
    }

    @Singleton
    fun listTripPlanApplication(
        tripPlanApplicationRepository: TripPlanApplicationRepository
    ): UseCase<ListTripPlanApplications.Input, ListTripPlanApplications.Output> {
        return ListTripPlanApplications(tripPlanApplicationRepository)
    }

}