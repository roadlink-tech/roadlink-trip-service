package com.roadlink.tripservice.config

import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.domain.trip_application.TripPlanApplicationRepository
import com.roadlink.tripservice.infrastructure.persistence.trip_application.InMemoryTripPlanApplicationRepository
import com.roadlink.tripservice.usecases.trip_plan.AcceptTripApplication
import com.roadlink.tripservice.usecases.trip_plan.CreateTripPlanApplication
import com.roadlink.tripservice.usecases.trip_plan.RejectTripApplication
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class TripApplicationDefinition {

    @Singleton
    fun tripPlanApplicationRepository(): TripPlanApplicationRepository {
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
    ): CreateTripPlanApplication {
        return CreateTripPlanApplication(sectionRepository, tripPlanApplicationRepository)
    }
}