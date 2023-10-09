package com.roadlink.tripservice.infrastructure.persistence

import com.roadlink.tripservice.domain.Location
import com.roadlink.tripservice.domain.trip.TripPlan
import com.roadlink.tripservice.domain.trip.section.Section
import com.roadlink.tripservice.domain.trip.section.SectionRepository
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import java.time.Instant
import java.util.*


//@Repository
//interface MicronautDataSectionRepository : CrudRepository<SectionJPAEntity, String>

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
        return entityManager.createQuery(
            """
                SELECT s 
                FROM SectionJPAEntity s 
                WHERE 
                    s.departure.latitude = :latitude AND
                    s.departure.longitude = :longitude AND
                    s.departure.estimatedArrivalTime >= :at 
            """.trimIndent(),
            SectionJPAEntity::class.java
        )
            .setParameter("latitude", from.latitude)
            .setParameter("longitude", from.longitude)
            .setParameter("at", at)
            .resultList
            .map { it.toDomain() }
            .toSet()
    }

    // TODO por qué usamos native query?
    override fun findAllById(sectionsIds: List<String>): List<Section> {
        return entityManager.createQuery(
            "SELECT s FROM SectionJPAEntity s WHERE s.id IN :sectionIds",
            SectionJPAEntity::class.java
        )
            .setParameter("sectionIds", sectionsIds)
            .resultList
            .map { it.toDomain() }
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

    override fun findAllByTripIds(tripIds: List<UUID>): Set<Section> {
        return entityManager.createQuery(
            "SELECT s FROM SectionJPAEntity s WHERE s.tripId in :tripIds",
            SectionJPAEntity::class.java
        )
            .setParameter("tripIds", tripIds)
            .resultList
            .map { it.toDomain() }
            .toSet()
    }
}