package com.roadlink.tripservice.config.trip_solicitude

import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.domain.trip_solicitude.TripLegSolicitudeRepository
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitudeRepository
import com.roadlink.tripservice.infrastructure.persistence.trip_solicitude.MySQLTripLegSolicitudeRepository
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
class TripDriverLegSolicitudeConfig {

    @Singleton
    fun tripApplicationRepository(
        entityManager: EntityManager,
        transactionManager: TransactionOperations<Session>
    ): TripLegSolicitudeRepository {
        return MySQLTripLegSolicitudeRepository(
            entityManager = entityManager,
            transactionManager = transactionManager
        )
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
    fun createTripPlanSolicitude(
        sectionRepository: SectionRepository,
        tripPlanSolicitudeRepository: TripPlanSolicitudeRepository
    ): UseCase<CreateTripPlanSolicitude.Input, CreateTripPlanSolicitude.Output> {
        return CreateTripPlanSolicitude(sectionRepository, tripPlanSolicitudeRepository)
    }

    @Singleton
    fun getTripPlanSolicitude(
        tripPlanSolicitudeRepository: TripPlanSolicitudeRepository
    ): UseCase<RetrieveTripPlanSolicitudeInput, RetrieveTripPlanSolicitudeOutput> {
        return RetrieveTripPlanSolicitude(tripPlanSolicitudeRepository)
    }

    @Singleton
    fun tripApplicationPlanResponseFactory(): TripPlanSolicitudeResponseFactory {
        return TripPlanSolicitudeResponseFactory()
    }
}