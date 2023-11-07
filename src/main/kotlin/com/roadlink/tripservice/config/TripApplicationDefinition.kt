package com.roadlink.tripservice.config

import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.domain.trip_application.TripApplicationRepository
import com.roadlink.tripservice.domain.trip_application.TripPlanApplicationRepository
import com.roadlink.tripservice.infrastructure.persistence.trip_application.MySQLTripApplicationRepository
import com.roadlink.tripservice.infrastructure.rest.trip_application.response.TripApplicationPlanResponseFactory
import com.roadlink.tripservice.usecases.UseCase
import com.roadlink.tripservice.usecases.trip_plan.*
import io.micronaut.context.annotation.Factory
import io.micronaut.transaction.TransactionOperations
import jakarta.inject.Singleton
import jakarta.persistence.EntityManager
import org.hibernate.Session
import java.util.*

@Factory
class TripApplicationDefinition {

    @Singleton
    fun tripApplicationRepository(entityManager: EntityManager, transactionManager: TransactionOperations<Session>): TripApplicationRepository {
        return MySQLTripApplicationRepository(entityManager = entityManager, transactionManager = transactionManager)
    }

    @Singleton
    fun acceptTripApplication(
        tripPlanApplicationRepository: TripPlanApplicationRepository
    ): UseCase<AcceptTripApplicationInput, AcceptTripApplicationOutput> {
        return AcceptTripApplication(tripPlanApplicationRepository)
    }

    @Singleton
    fun rejectTripApplication(
        tripPlanApplicationRepository: TripPlanApplicationRepository
    ): UseCase<UUID, RejectTripApplicationOutput> {
        return RejectTripApplication(tripPlanApplicationRepository)
    }

    @Singleton
    fun createTripPlanApplication(
        sectionRepository: SectionRepository,
        tripPlanApplicationRepository: TripPlanApplicationRepository
    ): UseCase<CreateTripPlanApplicationInput, CreateTripPlanApplicationOutput> {
        return CreateTripPlanApplication(sectionRepository, tripPlanApplicationRepository)
    }

    @Singleton
    fun tripApplicationPlanResponseFactory(): TripApplicationPlanResponseFactory {
        return TripApplicationPlanResponseFactory()
    }
}