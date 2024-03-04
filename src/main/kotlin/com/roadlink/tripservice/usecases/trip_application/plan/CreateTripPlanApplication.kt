package com.roadlink.tripservice.usecases.trip_application.plan

import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.domain.trip_application.TripPlanApplication
import com.roadlink.tripservice.domain.trip_application.TripPlanApplicationRepository
import com.roadlink.tripservice.usecases.UseCase
import com.roadlink.tripservice.usecases.trip_application.plan.CreateTripPlanApplicationOutput.OneOfTheSectionCanNotReceivePassenger
import com.roadlink.tripservice.usecases.trip_application.plan.CreateTripPlanApplicationOutput.TripPlanApplicationCreated
import java.util.*

class CreateTripPlanApplication(
    private val sectionRepository: SectionRepository,
    private val tripPlanApplicationRepository: TripPlanApplicationRepository
) : UseCase<CreateTripPlanApplicationInput, CreateTripPlanApplicationOutput> {

    override operator fun invoke(input: CreateTripPlanApplicationInput): CreateTripPlanApplicationOutput {
        val tripPlanApplication = TripPlanApplication()
        // TODO cuando creamos el trip plan, las seciiones deberían estar ordenadas por arrival time: primero la más proxima en el tiempo y después las más lejanas en el tiempo
        input.trips.forEach { tripSectionsDTO ->
            val sections = sectionRepository.findAllById(tripSectionsDTO.sectionsIds)
            sections.forEach { section ->
                if (!section.canReceiveAnyPassenger()) {
                    return OneOfTheSectionCanNotReceivePassenger(message = "The following section ${section.id} could not receive any passenger")
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
        return TripPlanApplicationCreated(tripPlanApplication.id)
    }
}

data class CreateTripPlanApplicationInput(
    val passengerId: String,
    val trips: List<TripSections>
) {
    data class TripSections(
        val tripId: String,
        val sectionsIds: Set<String>
    )
}

sealed class CreateTripPlanApplicationOutput {
    data class TripPlanApplicationCreated(val tripPlanApplicationId: UUID) : CreateTripPlanApplicationOutput()
    data class OneOfTheSectionCanNotReceivePassenger(val message: String) : CreateTripPlanApplicationOutput()
}
