package com.roadlink.tripservice.infrastructure.persistence.trip_plan

import com.roadlink.tripservice.domain.trip_plan.TripPlan
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.util.*

@Entity
@Table(
    name = "trip_leg",
)
data class TripLegJPAEntity(
    @Id
    @JdbcTypeCode(SqlTypes.CHAR)
    val id: UUID,
    @Column(name = "trip_id")
    @JdbcTypeCode(SqlTypes.CHAR)
    val tripId: UUID,
    @Column(name = "driver_id")
    @JdbcTypeCode(SqlTypes.CHAR)
    val driverId: UUID,
    @Column(name = "vehicle_id")
    @JdbcTypeCode(SqlTypes.CHAR)
    val vehicleId: UUID,
    @ManyToMany(
        cascade = [CascadeType.MERGE, CascadeType.REMOVE, CascadeType.REFRESH, CascadeType.DETACH],
        fetch = FetchType.EAGER
    )
    val sections: List<TripLegSectionJPAEntity>,
    @Column(name = "status")
    val status: String
) {

    fun toDomain(): TripPlan.TripLeg {
        return TripPlan.TripLeg(
            id = id,
            tripId = tripId,
            driverId = driverId,
            vehicleId = vehicleId,
            sections = sections.map { it.toDomain() },
            status = TripPlan.Status.valueOf(status)
        )
    }

    companion object {
        fun from(tripLeg: TripPlan.TripLeg): TripLegJPAEntity {
            return TripLegJPAEntity(
                id = tripLeg.id,
                tripId = tripLeg.tripId,
                driverId = tripLeg.driverId,
                vehicleId = tripLeg.vehicleId,
                sections = tripLeg.sections.map { TripLegSectionJPAEntity.from(it) },
                status = tripLeg.status.toString()
            )
        }
    }
}