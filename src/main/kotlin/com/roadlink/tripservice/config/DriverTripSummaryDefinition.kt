package com.roadlink.tripservice.config

import com.roadlink.tripservice.domain.trip.TripRepository
import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.domain.trip_application.TripApplicationRepository
import com.roadlink.tripservice.infrastructure.rest.trip_summary.DriverTripSummaryResponseFactory
import com.roadlink.tripservice.usecases.UseCase
import com.roadlink.tripservice.usecases.trip_summary.RetrieveDriverTripSummary
import com.roadlink.tripservice.usecases.trip_summary.RetrieveDriverTripSummaryOutput
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class DriverTripSummaryDefinition {
    @Singleton
    fun retrieveDriverTripSummary(
        tripRepository: TripRepository,
        sectionsRepository: SectionRepository,
        tripApplicationRepository: TripApplicationRepository
    ): UseCase<String, RetrieveDriverTripSummaryOutput> {
        return RetrieveDriverTripSummary(
            tripRepository,
            sectionsRepository,
            tripApplicationRepository
        )
    }

    @Singleton
    fun driverTripSummaryResponseFactory(): DriverTripSummaryResponseFactory {
        return DriverTripSummaryResponseFactory()
    }

}