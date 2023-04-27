package com.roadlink.tripservice.infrastructure.persistence

import com.roadlink.tripservice.usecases.JoinTripRequest
import com.roadlink.tripservice.usecases.JoinTripRequestRepository

class InMemoryJoinTripRequestRepository(
    private val joinTripRequests: MutableList<JoinTripRequest> = mutableListOf(),
) : JoinTripRequestRepository {
    override fun save(joinTripRequests: Set<JoinTripRequest>) {
        this.joinTripRequests.addAll(joinTripRequests)
    }

    override fun findBySectionAndPassenger(sectionId: String, passengerId: String): JoinTripRequest? =
        joinTripRequests
            .firstOrNull { it.sectionId == sectionId && it.passengerId == passengerId }

    fun findAll(): List<JoinTripRequest> = joinTripRequests
}