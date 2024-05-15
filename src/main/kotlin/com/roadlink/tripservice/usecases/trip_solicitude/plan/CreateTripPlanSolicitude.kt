package com.roadlink.tripservice.usecases.trip_solicitude.plan

import com.roadlink.tripservice.domain.trip.TripRepository
import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitudeRepository
import com.roadlink.tripservice.domain.user.UserError
import com.roadlink.tripservice.domain.user.UserRepository
import com.roadlink.tripservice.usecases.UseCase
import java.util.*

class CreateTripPlanSolicitude(
    private val sectionRepository: SectionRepository,
    private val tripRepository: TripRepository,
    private val userRepository: UserRepository,
    private val tripPlanSolicitudeRepository: TripPlanSolicitudeRepository
) : UseCase<CreateTripPlanSolicitude.Input, CreateTripPlanSolicitude.Output> {

    override operator fun invoke(input: Input): Output {
        val passenger = userRepository.findByUserId(id = input.passengerId)
            ?: throw UserError.NotExists(input.passengerId)
        val trips = tripRepository.find(TripRepository.CommandQuery(ids = input.tripIds()))

        if (trips.any { !it.canAdmitPassenger(passenger) }) {
            return Output.UserIsNotCompliantForJoiningTrip(
                message = "The user ${passenger.id} is not compliant for joining any trip."
            )
        }

        if (tripPlanSolicitudeAlreadySent(UUID.fromString(passenger.id), input.tripIds())) {
            return Output.TripPlanSolicitudeAlreadySent(
                message = "The user ${passenger.id} has already sent a solicitude."
            )
        }

        val tripPlanSolicitude = TripPlanSolicitude()
        // TODO cuando creamos el trip plan, las seciiones deberían estar ordenadas por arrival time: primero la más proxima en el tiempo y después las más lejanas en el tiempo
        input.tripSections.forEach { tripSectionsDTO ->
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

    private fun tripPlanSolicitudeAlreadySent(passengerId: UUID, tripIds: List<UUID>): Boolean {
        return tripPlanSolicitudeRepository.find(
            TripPlanSolicitudeRepository.CommandQuery(
                passengerId = passengerId, tripIds = tripIds
            )
        ).any { tripPlanSolicitude ->
            tripPlanSolicitude.tripLegSolicitudes.map { it.tripId() }.containsAll(tripIds)
        }
    }

    sealed class Output {
        data class TripPlanSolicitudeCreated(val tripPlanSolicitudeId: UUID) : Output()
        data class OneOfTheSectionCanNotReceivePassenger(val message: String) : Output()
        data class UserIsNotCompliantForJoiningTrip(val message: String) : Output()
        data class TripPlanSolicitudeAlreadySent(val message: String) : Output()
    }

    data class Input(
        val passengerId: String, val tripSections: List<TripSections>
    ) {
        data class TripSections(
            val tripId: String, val sectionsIds: Set<String>
        )

        fun tripIds(): List<UUID> = tripSections.map { UUID.fromString(it.tripId) }
    }
}