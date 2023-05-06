package com.roadlink.tripservice.usecases.trip_plan

import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.domain.trip_application.TripPlanApplication
import com.roadlink.tripservice.domain.trip_application.TripPlanApplicationRepository
import com.roadlink.tripservice.infrastructure.rest.trip_application.TripPlanApplicationDTO
import com.roadlink.tripservice.usecases.UseCase
import com.roadlink.tripservice.usecases.trip_plan.CreateTripPlanApplicationOutput.OneOfTheSectionCanNotReceivePassenger
import com.roadlink.tripservice.usecases.trip_plan.CreateTripPlanApplicationOutput.TripPlanApplicationCreated
import java.util.*

sealed class CreateTripPlanApplicationOutput {
    data class TripPlanApplicationCreated(val tripPlanApplicationId: UUID) : CreateTripPlanApplicationOutput()
    data class OneOfTheSectionCanNotReceivePassenger(val message: String) : CreateTripPlanApplicationOutput()
}

class CreateTripPlanApplication(
    private val sectionRepository: SectionRepository,
    private val tripPlanApplicationRepository: TripPlanApplicationRepository
) : UseCase<TripPlanApplicationDTO, CreateTripPlanApplicationOutput> {

    override operator fun invoke(input: TripPlanApplicationDTO): CreateTripPlanApplicationOutput {
        val tripPlanApplication = TripPlanApplication()
        input.trips.forEach { tripSectionsDTO ->
            val sections = sectionRepository.findAllById(tripSectionsDTO.sectionsIds.toSet())
            sections.forEach { section ->
                if (!section.canReceiveAnyPassenger()) {
                    return OneOfTheSectionCanNotReceivePassenger(message = "The following section ${section.id} could not receive any passenger")
                }
            }

            val tripApplication = TripPlanApplication.TripApplication(
                sections = sections,
                passengerId = input.passengerId,
                authorizerId = sections.first().driver
            )

            tripPlanApplication.include(tripApplication)
        }

        // TODO ver cómo informar o mostrar a los driver que tienen una solicitud pendiente de aceptación o rechazo
        tripPlanApplicationRepository.save(tripPlanApplication)
        return TripPlanApplicationCreated(tripPlanApplication.id)
    }
}
