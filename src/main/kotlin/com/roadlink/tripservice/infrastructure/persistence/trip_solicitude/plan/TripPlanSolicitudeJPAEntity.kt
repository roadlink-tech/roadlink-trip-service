package com.roadlink.tripservice.infrastructure.persistence.trip_solicitude.plan

import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude
import com.roadlink.tripservice.infrastructure.persistence.trip_solicitude.TripLegSolicitudeJPAEntity
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import org.locationtech.jts.geom.GeometryFactory
import java.util.*

@Entity
@Table(name = "trip_plan_solicitudes")
data class TripPlanSolicitudeJPAEntity(
    @Id
    @JdbcTypeCode(SqlTypes.CHAR)
    val id: UUID,
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "trip_plan_solicitudes_jpaentity_id", referencedColumnName = "id")
    val tripLegSolicitudes: List<TripLegSolicitudeJPAEntity>,
) {
    companion object {
        fun from(application: TripPlanSolicitude): TripPlanSolicitudeJPAEntity {
            return TripPlanSolicitudeJPAEntity(
                id = application.id,
                tripLegSolicitudes = application.tripLegSolicitudes.map { TripLegSolicitudeJPAEntity.from(it) }
            )
        }
    }

    fun toDomain(): TripPlanSolicitude {
        return TripPlanSolicitude(
            id = id,
            tripLegSolicitudes = tripLegSolicitudes.map { it.toDomain() }.toMutableList(),
        )
    }
}