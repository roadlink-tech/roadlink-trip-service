package com.roadlink.tripservice.config.trip_application

import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.domain.trip_solicitude.TripLegSolicitudeRepository
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitudeRepository
import com.roadlink.tripservice.infrastructure.persistence.trip_application.MySQLTripLegSolicitudeRepository
import com.roadlink.tripservice.infrastructure.rest.trip_solicitude.response.TripPlanSolicitudeResponseFactory
import com.roadlink.tripservice.usecases.UseCase
import com.roadlink.tripservice.usecases.trip_solicitude.*
import com.roadlink.tripservice.usecases.trip_solicitude.plan.*
import io.micronaut.context.annotation.Factory
import io.micronaut.transaction.TransactionOperations
import jakarta.inject.Singleton
import jakarta.persistence.EntityManager
import org.hibernate.Session
import java.util.*

@Factory
class TripApplicationConfig {

    @Singleton
    fun tripApplicationRepository(
        entityManager: EntityManager,
        transactionManager: TransactionOperations<Session>
    ): TripLegSolicitudeRepository {
        return MySQLTripLegSolicitudeRepository(entityManager = entityManager, transactionManager = transactionManager)
    }

    @Singleton
    fun acceptTripApplication(
        tripPlanSolicitudeRepository: TripPlanSolicitudeRepository
    ): UseCase<AcceptTripLegSolicitudeInput, AcceptTripLegSolicitudeOutput> {
        return AcceptTripLegSolicitude(tripPlanSolicitudeRepository)
    }

    @Singleton
    fun rejectTripApplication(
        tripPlanSolicitudeRepository: TripPlanSolicitudeRepository
    ): UseCase<UUID, RejectTripLegSolicitudeOutput> {
        return RejectTripLegSolicitude(tripPlanSolicitudeRepository)
    }

    @Singleton
    fun createTripPlanApplication(
        sectionRepository: SectionRepository,
        tripPlanSolicitudeRepository: TripPlanSolicitudeRepository
    ): UseCase<CreateTripPlanSolicitude.Input, CreateTripPlanSolicitude.Output> {
        return CreateTripPlanSolicitude(sectionRepository, tripPlanSolicitudeRepository)
    }

    @Singleton
    fun getTripPlanApplication(
        tripPlanSolicitudeRepository: TripPlanSolicitudeRepository
    ): UseCase<RetrieveTripPlanSolicitudeInput, RetrieveTripPlanSolicitudeOutput> {
        return RetrieveTripPlanApplication(tripPlanSolicitudeRepository)
    }

    @Singleton
    fun tripApplicationPlanResponseFactory(): TripPlanSolicitudeResponseFactory {
        return TripPlanSolicitudeResponseFactory()
    }
}