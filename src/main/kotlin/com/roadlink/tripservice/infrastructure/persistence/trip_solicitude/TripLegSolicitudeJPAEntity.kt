package com.roadlink.tripservice.infrastructure.persistence.trip_solicitude

import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude
import com.roadlink.tripservice.infrastructure.persistence.section.SectionJPAEntity
import jakarta.persistence.*
import jakarta.persistence.CascadeType.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import org.locationtech.jts.geom.GeometryFactory
import java.util.*

@Entity
@Table(
    name = "trip_leg_solicitudes",
)
data class TripLegSolicitudeJPAEntity(
    @Id
    @JdbcTypeCode(SqlTypes.CHAR)
    val id: UUID,
    @ManyToMany(cascade = [MERGE, REMOVE, REFRESH, DETACH], fetch = FetchType.EAGER)
    val sections: List<SectionJPAEntity>,
    @Column(name = "passenger_id")
    val passengerId: String,
    @Column(name = "status")
    val status: String,
    @Column(name = "authorizer_id")
    val authorizerId: String
) {
    companion object {
        fun from(application: TripPlanSolicitude.TripLegSolicitude, geometryFactory: GeometryFactory): TripLegSolicitudeJPAEntity {
            return TripLegSolicitudeJPAEntity(
                id = application.id,
                sections = application.sections.map { SectionJPAEntity.from(it, geometryFactory) },
                passengerId = application.passengerId,
                status = application.status.name,
                authorizerId = application.authorizerId,
            )
        }
    }

    fun toDomain(): TripPlanSolicitude.TripLegSolicitude {
        return TripPlanSolicitude.TripLegSolicitude(
            id = id,
            sections = sections.map { it.toDomain() },
            passengerId = passengerId,
            status = TripPlanSolicitude.TripLegSolicitude.Status.valueOf(status),
            authorizerId = authorizerId,
        )
    }

}