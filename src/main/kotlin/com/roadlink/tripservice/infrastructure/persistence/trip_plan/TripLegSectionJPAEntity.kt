package com.roadlink.tripservice.infrastructure.persistence.trip_plan

import com.roadlink.tripservice.domain.trip_plan.TripLegSection
import com.roadlink.tripservice.infrastructure.persistence.common.TripPointJPAEntity
import jakarta.persistence.AttributeOverride
import jakarta.persistence.AttributeOverrides
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "trip_leg_sections")
data class TripLegSectionJPAEntity(
    @Id val id: String,
    @Embedded
    @AttributeOverrides(
        AttributeOverride(
            name = "estimatedArrivalTime",
            column = Column(name = "departure_estimated_arrival_time")
        ),
        AttributeOverride(name = "fullAddress", column = Column(name = "departure_full_address")),
        AttributeOverride(name = "street", column = Column(name = "departure_street")),
        AttributeOverride(name = "city", column = Column(name = "departure_city")),
        AttributeOverride(name = "country", column = Column(name = "departure_country")),
        AttributeOverride(name = "houseNumber", column = Column(name = "departure_house_number")),
        AttributeOverride(name = "latitude", column = Column(name = "departure_latitude")),
        AttributeOverride(name = "longitude", column = Column(name = "departure_longitude"))
    )
    val departure: TripPointJPAEntity,
    @Embedded
    @AttributeOverrides(
        AttributeOverride(
            name = "estimatedArrivalTime",
            column = Column(name = "arrival_estimated_arrival_time")
        ),
        AttributeOverride(name = "fullAddress", column = Column(name = "arrival_full_address")),
        AttributeOverride(name = "street", column = Column(name = "arrival_street")),
        AttributeOverride(name = "city", column = Column(name = "arrival_city")),
        AttributeOverride(name = "country", column = Column(name = "arrival_country")),
        AttributeOverride(name = "houseNumber", column = Column(name = "arrival_house_number")),
        AttributeOverride(name = "latitude", column = Column(name = "arrival_latitude")),
        AttributeOverride(name = "longitude", column = Column(name = "arrival_longitude"))
    )
    val arrival: TripPointJPAEntity,
    @Column(name = "distance_in_meters")
    val distanceInMeters: Double,
) {

    fun toDomain(): TripLegSection {
        return TripLegSection(
            id = id,
            departure = departure.toDomain(),
            arrival = departure.toDomain(),
            distanceInMeters = distanceInMeters
        )
    }

    companion object {
        fun from(tripLegSection: TripLegSection): TripLegSectionJPAEntity {
            return TripLegSectionJPAEntity(
                id = tripLegSection.id,
                departure = TripPointJPAEntity.from(tripLegSection.departure),
                arrival = TripPointJPAEntity.from(tripLegSection.arrival),
                distanceInMeters = tripLegSection.distanceInMeters
            )
        }
    }
}