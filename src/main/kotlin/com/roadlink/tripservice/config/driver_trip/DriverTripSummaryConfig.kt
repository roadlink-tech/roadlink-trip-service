package com.roadlink.tripservice.config.driver_trip

import com.roadlink.tripservice.domain.trip.TripRepository
import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.domain.trip_solicitude.TripLegSolicitudeRepository
import com.roadlink.tripservice.infrastructure.rest.driver_trip.response.DriverTripSummaryResponseFactory
import com.roadlink.tripservice.usecases.UseCase
import com.roadlink.tripservice.usecases.driver_trip.RetrieveDriverTripSummary
import com.roadlink.tripservice.usecases.driver_trip.RetrieveDriverTripSummaryOutput
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class DriverTripSummaryConfig {
    @Singleton
    fun retrieveDriverTripSummary(
        tripRepository: TripRepository,
        sectionsRepository: SectionRepository,
        tripLegSolicitudeRepository: TripLegSolicitudeRepository
    ): UseCase<String, RetrieveDriverTripSummaryOutput> {
        return RetrieveDriverTripSummary(
            tripRepository,
            sectionsRepository,
            tripLegSolicitudeRepository
        )
    }

    @Singleton
    fun driverTripSummaryResponseFactory(): DriverTripSummaryResponseFactory {
        return DriverTripSummaryResponseFactory()
    }

}