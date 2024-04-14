package com.roadlink.tripservice.infrastructure.persistence.trip_solicitude.plan

import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitudeRepository
import com.roadlink.tripservice.infrastructure.persistence.section.SectionJPAEntity
import com.roadlink.tripservice.infrastructure.persistence.trip_solicitude.TripLegSolicitudeJPAEntity
import io.micronaut.transaction.TransactionOperations
import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.Predicate
import org.hibernate.Session
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.PrecisionModel
import java.util.*


class MySQLTripPlanSolicitudeRepository(
    private val entityManager: EntityManager,
    private val transactionManager: TransactionOperations<Session>,
) : TripPlanSolicitudeRepository {

    override fun insert(tripPlanSolicitude: TripPlanSolicitude) {
        transactionManager.executeWrite {
            entityManager.persist(TripPlanSolicitudeJPAEntity.from(tripPlanSolicitude))
        }
    }

    override fun update(tripPlanSolicitude: TripPlanSolicitude) {
        transactionManager.executeWrite {
            entityManager.merge(TripPlanSolicitudeJPAEntity.from(tripPlanSolicitude))
        }
    }

    override fun find(commandQuery: TripPlanSolicitudeRepository.CommandQuery): List<TripPlanSolicitude> {
        val cq = TripPlanSolicitudeCommandQuery.from(commandQuery)
        return transactionManager.executeRead {
            val cb = entityManager.criteriaBuilder
            val criteriaQuery = cb.createQuery(TripPlanSolicitudeJPAEntity::class.java)
            val root = criteriaQuery.from(TripPlanSolicitudeJPAEntity::class.java)

            val predicates = mutableListOf<Predicate>()

            if (cq.ids.isNotEmpty()) {
                val idPredicate = root.get<UUID>("id").`in`(cq.ids)
                predicates.add(idPredicate)
            }

            if (cq.passengerId != null) {
                val tripApplicationsJoin =
                    root.join<TripPlanSolicitudeJPAEntity, TripLegSolicitudeJPAEntity>("tripLegSolicitudes")
                val passengerIdPredicate =
                    cb.equal(
                        tripApplicationsJoin.get<String>("passengerId"),
                        cq.passengerId.toString()
                    )
                predicates.add(passengerIdPredicate)
            }

            if (cq.tripLegSolicitudeId != null) {
                val tripApplicationsJoin =
                    root.join<TripPlanSolicitudeJPAEntity, TripLegSolicitudeJPAEntity>("tripLegSolicitudes")
                val tripApplicationIdPredicate =
                    cb.equal(tripApplicationsJoin.get<UUID>("id"), cq.tripLegSolicitudeId)
                predicates.add(tripApplicationIdPredicate)
            }

            if (cq.tripId != null) {
                val tripApplicationsJoin =
                    root.join<TripPlanSolicitudeJPAEntity, TripLegSolicitudeJPAEntity>("tripLegSolicitudes")
                val sectionsJoin =
                    tripApplicationsJoin.join<TripLegSolicitudeJPAEntity, SectionJPAEntity>("sections")
                val tripIdPredicate = cb.equal(sectionsJoin.get<UUID>("tripId"), cq.tripId)
                predicates.add(tripIdPredicate)
            }

            criteriaQuery.select(root).where(cb.and(*predicates.toTypedArray()))

            entityManager.createQuery(criteriaQuery).resultList.map { it.toDomain() }
        }

    }

    data class TripPlanSolicitudeCommandQuery(
        val ids: List<UUID> = emptyList(),
        val tripLegSolicitudeId: UUID? = null,
        val passengerId: UUID? = null,
        val tripId: UUID? = null
    ) {
        init {
            require(ids.isNotEmpty() || tripLegSolicitudeId != null || passengerId != null || tripId != null) {
                "At least one field must be not null or empty"
            }
        }

        companion object {

            fun from(domainCommandQuery: TripPlanSolicitudeRepository.CommandQuery): TripPlanSolicitudeCommandQuery {
                return TripPlanSolicitudeCommandQuery(
                    ids = domainCommandQuery.ids,
                    tripLegSolicitudeId = domainCommandQuery.tripLegSolicitudeId,
                    passengerId = domainCommandQuery.passengerId,
                    tripId = domainCommandQuery.tripId
                )
            }
        }
    }
}
