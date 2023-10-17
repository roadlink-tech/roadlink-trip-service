package com.roadlink.tripservice.infrastructure.persistence

import com.roadlink.tripservice.domain.Location
import com.roadlink.tripservice.domain.trip.TripPlan
import com.roadlink.tripservice.domain.trip.section.Section
import com.roadlink.tripservice.domain.trip.section.SectionRepository
import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.Predicate
import jakarta.transaction.Transactional
import java.time.Instant
import java.util.*


class MySQLSectionRepository(
    private val entityManager: EntityManager
) : SectionRepository {

    @Transactional
    override fun save(section: Section) {
        entityManager.persist(SectionJPAEntity.from(section))
    }

    override fun saveAll(sections: Set<Section>) {
        // TODO: mejorar esto para que el insert sea en batch
        for (section in sections) {
            entityManager.persist(SectionJPAEntity.from(section))
        }
    }

    override fun findNextSections(from: Location, at: Instant): Set<Section> {
        return find(
            SectionCommandQuery(
                departureLatitude = from.latitude,
                departureLongitude = from.longitude,
                departureAfterInstant = at
            )
        ).map { it.toDomain() }.toSet()
    }

    // TODO sectionID must be a UUID
    override fun findAllById(sectionsIds: Set<String>): List<Section> {
        return find(SectionCommandQuery(sectionsIds = sectionsIds)).map { it.toDomain() }
    }

    // TODO esto lo podemos mover a un repositorio de TripPlan
    override fun findByTripId(tripId: UUID): TripPlan {
        return entityManager.createQuery(
            "SELECT s FROM SectionJPAEntity s WHERE s.tripId = :tripId ORDER BY s.departure.estimatedArrivalTime",
            SectionJPAEntity::class.java
        )
            .setParameter("tripId", tripId)
            .resultList
            .map { it.toDomain() }
            .let { sections ->
                TripPlan(sections)
            }
    }

    override fun findAllByTripIds(tripIds: Set<UUID>): Set<Section> {
        return find(SectionCommandQuery(tripIds = tripIds)).map { it.toDomain() }.toSet()
    }

    private fun find(commandQuery: SectionCommandQuery): Collection<SectionJPAEntity> {
        val cb = entityManager.criteriaBuilder
        val criteriaQuery = cb.createQuery(SectionJPAEntity::class.java)
        val root = criteriaQuery.from(SectionJPAEntity::class.java)

        val predicates = mutableListOf<Predicate>()

        if (commandQuery.tripIds.isNotEmpty()) {
            predicates.add(root.get<UUID>("tripId").`in`(commandQuery.tripIds))
        }

        if (commandQuery.sectionsIds.isNotEmpty()) {
            predicates.add(root.get<String>("id").`in`(commandQuery.sectionsIds))
        }

        if (commandQuery.departureLatitude != null) {
            predicates.add(
                root.get<TripPointJPAEntity>("departure").get<Double>("latitude").`in`(commandQuery.departureLatitude)
            )
        }

        if (commandQuery.departureLongitude != null) {
            predicates.add(
                root.get<TripPointJPAEntity>("departure").get<Double>("longitude").`in`(commandQuery.departureLongitude)
            )
        }

        if (commandQuery.departureAfterInstant != null) {
            predicates.add(
                cb.greaterThanOrEqualTo(
                    root.get<TripPointJPAEntity>("departure").get("estimatedArrivalTime"),
                    commandQuery.departureAfterInstant
                )
            )
        }

        criteriaQuery
            .select(root)
            .where(cb.and(*predicates.toTypedArray()))
            .orderBy(cb.desc(root.get<TripPointJPAEntity>("departure").get<Instant>("estimatedArrivalTime")))
        return entityManager.createQuery(criteriaQuery).resultList
    }
}

data class SectionCommandQuery(
    val tripIds: Collection<UUID> = emptySet(),
    val sectionsIds: Collection<String> = emptySet(),
    val departureLatitude: Double? = null,
    val departureLongitude: Double? = null,
    val departureAfterInstant: Instant? = null
)