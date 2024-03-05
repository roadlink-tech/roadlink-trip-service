package com.roadlink.tripservice.usecases.trip_application.plan

import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.domain.trip_application.TripPlanApplication
import com.roadlink.tripservice.domain.trip_application.TripPlanApplicationRepository
import com.roadlink.tripservice.usecases.UseCase
import java.util.*

class CreateTripPlanApplication(
    private val sectionRepository: SectionRepository,
    private val tripPlanApplicationRepository: TripPlanApplicationRepository
) : UseCase<CreateTripPlanApplication.Input, CreateTripPlanApplication.Output> {

    override operator fun invoke(input: Input): Output {
        val tripPlanApplication = TripPlanApplication()
        // TODO cuando creamos el trip plan, las seciiones deberían estar ordenadas por arrival time: primero la más proxima en el tiempo y después las más lejanas en el tiempo
        input.trips.forEach { tripSectionsDTO ->
            val sections = sectionRepository.findAllById(tripSectionsDTO.sectionsIds)
            sections.forEach { section ->
                if (!section.canReceiveAnyPassenger()) {
                    return Output.OneOfTheSectionCanNotReceivePassenger(message = "The following section ${section.id} could not receive any passenger")
                }
            }

            val tripApplication = TripPlanApplication.TripApplication(
                sections = sections,
                passengerId = input.passengerId,
                authorizerId = sections.first().driverId
            )

            tripPlanApplication.include(tripApplication)
        }

        // TODO ver cómo informar o mostrar a los driver que tienen una solicitud pendiente de aceptación o rechazo
        tripPlanApplicationRepository.insert(tripPlanApplication)
        return Output.TripPlanApplicationCreated(tripPlanApplication.id)
    }

    sealed class Output {
        data class TripPlanApplicationCreated(val tripPlanApplicationId: UUID) : Output()
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