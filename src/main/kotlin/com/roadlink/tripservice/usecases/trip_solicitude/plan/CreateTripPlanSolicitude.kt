package com.roadlink.tripservice.usecases.trip_solicitude.plan

import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitudeRepository
import com.roadlink.tripservice.usecases.UseCase
import java.util.*

class CreateTripPlanSolicitude(
    private val sectionRepository: SectionRepository,
    private val tripPlanSolicitudeRepository: TripPlanSolicitudeRepository
) : UseCase<CreateTripPlanSolicitude.Input, CreateTripPlanSolicitude.Output> {

    override operator fun invoke(input: Input): Output {
        val tripPlanSolicitude = TripPlanSolicitude()
        // TODO cuando creamos el trip plan, las seciiones deberían estar ordenadas por arrival time: primero la más proxima en el tiempo y después las más lejanas en el tiempo
        input.trips.forEach { tripSectionsDTO ->
            val sections = sectionRepository.findAllById(tripSectionsDTO.sectionsIds)
            sections.forEach { section ->
                if (!section.canReceiveAnyPassenger()) {
                    return Output.OneOfTheSectionCanNotReceivePassenger(message = "The following section ${section.id} could not receive any passenger")
                }
            }

            val tripLegSolicitude = TripPlanSolicitude.TripLegSolicitude(
                sections = sections,
                passengerId = input.passengerId,
                authorizerId = sections.first().driverId
            )

            tripPlanSolicitude.include(tripLegSolicitude)
        }

        // TODO ver cómo informar o mostrar a los driver que tienen una solicitud pendiente de aceptación o rechazo
        tripPlanSolicitudeRepository.insert(tripPlanSolicitude)
        return Output.TripPlanSolicitudeCreated(tripPlanSolicitude.id)
    }

    sealed class Output {
        data class TripPlanSolicitudeCreated(val tripPlanSolicitudeId: UUID) : Output()
        data class OneOfTheSectionCanNotReceivePassenger(val message: String) : Output()
    }

    data class Input(
        val passengerId: String,
        val trips: List<TripSections>
    ) {
        data class TripSections(
            val tripId: String,
            val sectionsIds: Set<String>
        )
    }

}