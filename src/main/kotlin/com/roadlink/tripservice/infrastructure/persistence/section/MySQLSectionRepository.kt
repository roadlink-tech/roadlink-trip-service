package com.roadlink.tripservice.infrastructure.persistence.section

import com.roadlink.tripservice.domain.common.Location
import com.roadlink.tripservice.domain.trip.section.Section
import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.infrastructure.persistence.common.Jts
import com.roadlink.tripservice.infrastructure.persistence.common.TripPointJPAEntity
import io.micronaut.transaction.TransactionOperations
import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.*
import org.hibernate.Session
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Point
import org.locationtech.jts.geom.Polygon
import org.locationtech.jts.geom.PrecisionModel
import org.locationtech.jts.util.GeometricShapeFactory
import java.time.Instant
import java.util.*


class MySQLSectionRepository(
    private val entityManager: EntityManager,
    private val transactionManager: TransactionOperations<Session>,
) : SectionRepository {

    private val geometryFactory = GeometryFactory(PrecisionModel(), Jts.SRID)
    private val geometricShapeFactory = GeometricShapeFactory(geometryFactory)

    private val maxNearRadiusInMeters = 10000.0
    private val nearSearchCircleNumPoints = 32

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

    override fun findNextSections(from: Location, at: Instant): Set<Section> {
        return transactionManager.executeRead {
            find(
                SectionCommandQuery(
                    departureLatitude = from.latitude,
                    departureLongitude = from.longitude,
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

        if (commandQuery.departureLatitude != null && commandQuery.departureLongitude != null) {
            val nearSearchCircle = createNearSearchCircle(commandQuery.departureLongitude, commandQuery.departureLatitude)

            predicates.add(
                cb.isTrue(
                    cb.function("ST_Contains",
                        Boolean::class.java,
                        cb.literal(nearSearchCircle),
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

    private fun createNearSearchCircle(longitude: Double, latitude: Double): Polygon {
        val coordinate = Coordinate(longitude, latitude)

        geometricShapeFactory.setCentre(coordinate)
        geometricShapeFactory.setSize(2 * toDegrees(maxNearRadiusInMeters))
        geometricShapeFactory.setNumPoints(nearSearchCircleNumPoints)
        return geometricShapeFactory.createCircle()
    }

    private fun toDegrees(meters: Double): Double =
        meters / 110000.0

}

data class SectionCommandQuery(
    val tripIds: Collection<UUID> = emptySet(),
    val sectionsIds: Collection<String> = emptySet(),
    val departureLatitude: Double? = null,
    val departureLongitude: Double? = null,
    val departureAfterInstant: Instant? = null
)