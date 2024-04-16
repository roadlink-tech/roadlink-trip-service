package com.roadlink.tripservice.infrastructure.persistence.section

import com.roadlink.tripservice.domain.trip.section.Section
import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.infrastructure.persistence.common.TripPointJPAEntity
import io.micronaut.transaction.TransactionOperations
import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.Predicate
import org.hibernate.Session
import org.locationtech.jts.geom.Point
import org.locationtech.jts.geom.Polygon
import java.time.Instant
import java.util.*

class MySQLSectionRepository(
    private val entityManager: EntityManager,
    private val transactionManager: TransactionOperations<Session>,
) : SectionRepository {

    override fun save(section: Section) {
        transactionManager.executeWrite {
            entityManager.persist(SectionJPAEntity.from(section))
        }
    }

    override fun saveAll(sections: Set<Section>) {
        // TODO: mejorar esto para que el insert sea en batch
        transactionManager.executeWrite {
            for (section in sections) {
                entityManager.persist(SectionJPAEntity.from(section))
            }
        }
    }

    override fun findNextSectionsIn(polygon: Polygon, at: Instant): Set<Section> {
        return transactionManager.executeRead {
            find(
                SectionCommandQuery(
                    polygonContainsDeparturePoint = polygon,
                    departureAfterInstant = at
                )
            ).map { it.toDomain() }.toSet()
        }
    }

    // TODO sectionID must be a UUID
    override fun findAllById(sectionsIds: Set<String>): List<Section> {
        return transactionManager.executeRead {
            find(SectionCommandQuery(sectionsIds = sectionsIds)).map { it.toDomain() }
        }
    }

    override fun findAllByTripIdOrFail(tripId: UUID): List<Section> {
        return transactionManager.executeRead {
            find(SectionCommandQuery(tripIds = listOf(tripId))).map { it.toDomain() }
        }
    }

    override fun findAllByTripIds(tripIds: Set<UUID>): Set<Section> {
        return transactionManager.executeRead {
            find(SectionCommandQuery(tripIds = tripIds)).map { it.toDomain() }.toSet()
        }
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

        if (commandQuery.polygonContainsDeparturePoint != null) {
            predicates.add(
                cb.isTrue(
                    cb.function("ST_Contains",
                        Boolean::class.java,
                        cb.literal(commandQuery.polygonContainsDeparturePoint),
                        root.get<Point>("departurePoint"),
                    )
                )
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
    val departureAfterInstant: Instant? = null,
    val polygonContainsDeparturePoint: Polygon? = null
)