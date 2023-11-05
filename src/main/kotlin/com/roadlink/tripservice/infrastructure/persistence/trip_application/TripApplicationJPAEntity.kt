package com.roadlink.tripservice.infrastructure.persistence.trip_application

import com.roadlink.tripservice.domain.trip_application.TripPlanApplication
import com.roadlink.tripservice.infrastructure.persistence.SectionJPAEntity
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.util.*

@Entity
@Table(name = "trip_application")
data class TripApplicationJPAEntity(
    @Id
    @JdbcTypeCode(SqlTypes.CHAR)
    val id: UUID,
    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    val sections: List<SectionJPAEntity>,
    val passengerId: String,
    val status: String,
    val authorizerId: String
) {
    companion object {
        fun from(application: TripPlanApplication.TripApplication): TripApplicationJPAEntity {
            return TripApplicationJPAEntity(
                id = application.id,
                sections = application.sections.map { SectionJPAEntity.from(it) },
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