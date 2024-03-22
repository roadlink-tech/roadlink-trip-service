package com.roadlink.tripservice.infrastructure.persistence.trip_plan

import com.roadlink.tripservice.domain.trip_plan.TripPlan
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.util.*

@Entity
@Table(name = "trip_plan")
data class TripPlanJPAEntity(
    @Id
    @JdbcTypeCode(SqlTypes.CHAR)
    val id: UUID,
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(
        name = "passenger_id",
        nullable = false,
        updatable = false,
        columnDefinition = "VARCHAR(36)"
    )
    val passengerId: UUID,
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "trip_plan_jpaentity_id", referencedColumnName = "id")
    val tripLegs: List<TripLegJPAEntity>,
) {

    fun toDomain(): TripPlan {
        return TripPlan(
            id = id,
            passengerId = passengerId,
            tripLegs = tripLegs.map { it.toDomain() }
        )
    }

    companion object {
        fun from(tripPlan: TripPlan): TripPlanJPAEntity {
            return TripPlanJPAEntity(
                id = tripPlan.id,
                passengerId = tripPlan.passengerId,
                tripLegs = tripPlan.tripLegs.map { TripLegJPAEntity.from(it) }
            )
        }
    }
}

