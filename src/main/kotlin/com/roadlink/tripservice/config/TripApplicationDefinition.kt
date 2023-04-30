package com.roadlink.tripservice.config

import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.domain.trip_application.TripPlanApplicationRepository
import com.roadlink.tripservice.infrastructure.persistence.trip_application.InMemoryTripPlanApplicationRepository
import com.roadlink.tripservice.infrastructure.rest.trip_application.TripPlanApplicationDTO
import com.roadlink.tripservice.infrastructure.rest.trip_application.response.TripApplicationPlanResponseFactory
import com.roadlink.tripservice.usecases.UseCase
import com.roadlink.tripservice.usecases.trip_plan.AcceptTripApplication
import com.roadlink.tripservice.usecases.trip_plan.CreateTripPlanApplication
import com.roadlink.tripservice.usecases.trip_plan.CreateTripPlanApplicationOutput
import com.roadlink.tripservice.usecases.trip_plan.RejectTripApplication
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class TripApplicationDefinition {

    @Singleton
    fun tripPlanApplicationRepository(): InMemoryTripPlanApplicationRepository {
        return InMemoryTripPlanApplicationRepository()
    }

    @Singleton
    fun acceptTripApplication(tripPlanApplicationRepository: TripPlanApplicationRepository): AcceptTripApplication {
        return AcceptTripApplication(tripPlanApplicationRepository)
    }

    @Singleton
    fun rejectTripApplication(tripPlanApplicationRepository: TripPlanApplicationRepository): RejectTripApplication {
        return RejectTripApplication(tripPlanApplicationRepository)
    }

    @Singleton
    fun createTripPlanApplication(
        sectionRepository: SectionRepository,
        tripPlanApplicationRepository: TripPlanApplicationRepository
    ): UseCase<TripPlanApplicationDTO, CreateTripPlanApplicationOutput> {
        return CreateTripPlanApplication(sectionRepository, tripPlanApplicationRepository)
    }

    @Singleton
    fun tripApplicationPlanResponseFactory(): TripApplicationPlanResponseFactory {
        return TripApplicationPlanResponseFactory()
    }
}