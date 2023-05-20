package com.roadlink.tripservice.usecases

import com.roadlink.tripservice.domain.IdGenerator
import com.roadlink.tripservice.domain.trip.AlreadyRequestedJoinTrip

class RequestJoinTrip(
    private val idGenerator: IdGenerator,
    private val joinTripRequestRepository: JoinTripRequestRepository,
) {
    operator fun invoke(input: Input): Set<JoinTripRequest> {
        val jointTripRequests = input.sectionIds.map { sectionId ->
            checkAlreadyRequestedJoinTrip(sectionId, input.passengerId)
            JoinTripRequest(
                id = idGenerator.id(),
                passengerId = input.passengerId,
                sectionId = sectionId,
            )
        }.toSet()

        joinTripRequestRepository.save(jointTripRequests)

        return jointTripRequests
    }

    private fun checkAlreadyRequestedJoinTrip(sectionId: String, passengerId: String) {
        joinTripRequestRepository.findBySectionAndPassenger(sectionId, passengerId)?.let {
            throw AlreadyRequestedJoinTrip()
        }
    }

    data class Input(
        val sectionIds: Set<String>,
        val passengerId: String,
    )
}

data class JoinTripRequest(
    val id: String,
    val passengerId: String,
    val sectionId: String,
)

interface JoinTripRequestRepository {
    fun save(joinTripRequests: Set<JoinTripRequest>)
    fun findBySectionAndPassenger(sectionId: String, passengerId: String): JoinTripRequest?
}
