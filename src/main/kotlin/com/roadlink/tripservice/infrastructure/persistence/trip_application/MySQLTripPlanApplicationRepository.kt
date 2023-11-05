package com.roadlink.tripservice.infrastructure.persistence.trip_application

import com.roadlink.tripservice.domain.trip_application.TripApplicationRepository
import com.roadlink.tripservice.domain.trip_application.TripPlanApplication
import com.roadlink.tripservice.domain.trip_application.TripPlanApplicationRepository
import com.roadlink.tripservice.infrastructure.persistence.SectionJPAEntity
import jakarta.persistence.EntityManager
import java.util.*
import jakarta.persistence.*
import jakarta.transaction.Transactional
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes


@Entity
@Table(name = "trip_plan_application")
data class TripPlanApplicationJPAEntity(
    @Id
    @JdbcTypeCode(SqlTypes.CHAR)
    val id: UUID,
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER) //TODO: tiene sentido esto aca? dado que al guardar primero tenemos que guardar a parte esta collection
    @JoinColumn(name = "trip_plan_application_jpaentity_id", referencedColumnName = "id")
    val tripApplications: List<TripApplicationJPAEntity>,
) {
    companion object {
        fun from(application: TripPlanApplication): TripPlanApplicationJPAEntity {
            return TripPlanApplicationJPAEntity(
                id = application.id,
                tripApplications = application.tripApplications.map { TripApplicationJPAEntity.from(it) } //TODO: tiene sentido esto aca? dado que al guardar primero tenemos que guardar a parte esta collection
            )
        }
    }

    fun toDomain(): TripPlanApplication {
        return TripPlanApplication(
            id = id,
            tripApplications = tripApplications.map { it.toDomain() }.toMutableList(),
        )
    }
}

@Entity
@Table(name = "trip_application")
data class TripApplicationJPAEntity(
    @Id
    @JdbcTypeCode(SqlTypes.CHAR)
    val id: UUID,
    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER) //TODO: tiene sentido esto aca? dado que al guardar primero tenemos que guardar a parte esta collection
    val sections: List<SectionJPAEntity>,
    val passengerId: String,
    val status: String,
    // TODO qué es esto?
    val authorizerId: String
) {
    companion object {
        fun from(application: TripPlanApplication.TripApplication): TripApplicationJPAEntity {
            return TripApplicationJPAEntity(
                id = application.id,
                sections = application.sections.map { SectionJPAEntity.from(it) }, //TODO: tiene sentido esto aca? dado que al guardar primero tenemos que guardar a parte esta collection
                passengerId = application.passengerId,
                status = application.status.name,
                authorizerId = application.authorizerId,
            )
        }
    }

    fun toDomain(): TripPlanApplication.TripApplication {
        return TripPlanApplication.TripApplication(
            id = id,
            sections = sections.map { it.toDomain() },
            passengerId = passengerId,
            status = TripPlanApplication.TripApplication.Status.valueOf(status),
            authorizerId = authorizerId,
        )
    }

}


class MySQLTripApplicationRepository(
    private val entityManager: EntityManager,
) : TripApplicationRepository {
    override fun saveAll(tripApplications: List<TripPlanApplication.TripApplication>) {
        for (tripApplication in tripApplications) {
            entityManager.persist(TripApplicationJPAEntity.from(tripApplication))
        }
    }

    override fun findAllByDriverId(driverId: UUID): List<TripPlanApplication.TripApplication> {
        TODO("Not yet implemented")
    }

    override fun findByTripId(tripId: UUID): List<TripPlanApplication.TripApplication> {
        TODO("Not yet implemented")
    }

}


class MySQLTripPlanApplicationRepository(
    private val entityManager: EntityManager,
    private val mySQLTripApplicationRepository: MySQLTripApplicationRepository,
): TripPlanApplicationRepository {

    override fun insert(application: TripPlanApplication) {
        //mySQLTripApplicationRepository.saveAll(application.tripApplications)

        entityManager.persist(TripPlanApplicationJPAEntity.from(application))
    }

    override fun update(application: TripPlanApplication) {
        entityManager.merge(TripPlanApplicationJPAEntity.from(application))
    }

    override fun findByTripApplicationId(tripApplicationId: UUID): TripPlanApplication? {
        return try {
            entityManager.createQuery(
                "SELECT tpa FROM TripPlanApplicationJPAEntity tpa WHERE tpa.id = :id",
                TripPlanApplicationJPAEntity::class.java
            )
                .setParameter("id", tripApplicationId)
                .singleResult
                .toDomain()
        } catch (e: NoResultException) {
            null
        }
    }

    override fun findTripApplicationBySectionId(sectionId: String): Set<TripPlanApplication.TripApplication> {
        return entityManager.createQuery(
            """
                |SELECT ta FROM TripApplicationJPAEntity ta
                |JOIN ta.sections s
                |WHERE s.id = :sectionId
                |""".trimMargin(),
            TripApplicationJPAEntity::class.java
        )
            .setParameter("sectionId", sectionId)
            .resultList
            .map { it.toDomain() }
            .toSet()
    }

}
