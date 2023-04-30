package com.roadlink.tripservice.usecases

import com.roadlink.tripservice.domain.IdGenerator
import com.roadlink.tripservice.domain.trip.AlreadyRequestedJoinTrip

/*

Trip A
    Section 1: A -> B
    Section 2: B -> C
    Driver: mbosch

Trip B
    Section 3: C -> D
    Driver: mbosch

Trip C
    Section 4: C -> F
    Section 5: F -> G
    Driver: jreyero

Request:
    Trip A:
        Section 1: A -> B
        Section 2: B -> C
    Trip C
        Section 4: C -> F

------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------

SE ACEPTA SOLICITUD A

    solicitud = buscar solicitud A

    solicitudes = encontrar todas las solicitudes asociadas solicitud.agrupador
    para cada solicitud en solicitudes {
        si solicitud ya esta rechazada:
            terminar
    }

    secciones = obtener secciones de solicitud
    para cada seccion en secciones {
        ocupar asiento de section
    }

    si todas las solicitud en solictudes estan aprobadas :
        confirmar passenger trip plan

    guardar el TripPlan
 */




// TODO delete id!

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