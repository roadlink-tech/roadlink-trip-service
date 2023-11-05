package com.roadlink.tripservice.infrastructure.persistence.trip_application

import com.roadlink.tripservice.domain.trip_application.TripPlanApplication
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.util.*

@Entity
@Table(name = "trip_plan_application")
data class TripPlanApplicationJPAEntity(
    @Id
    @JdbcTypeCode(SqlTypes.CHAR)
    val id: UUID,
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "trip_plan_application_jpaentity_id", referencedColumnName = "id")
    val tripApplications: List<TripApplicationJPAEntity>,
) {
    companion object {
        fun from(application: TripPlanApplication): TripPlanApplicationJPAEntity {
            return TripPlanApplicationJPAEntity(
                id = application.id,
                tripApplications = application.tripApplications.map { TripApplicationJPAEntity.from(it) }
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