package com.roadlink.tripservice.infrastructure.persistence.trip.constraint

import com.roadlink.tripservice.domain.trip.constraint.Constraint
import com.roadlink.tripservice.domain.trip.constraint.Restriction
import com.roadlink.tripservice.domain.trip.constraint.Visibility
import jakarta.persistence.Converter
import jakarta.persistence.Embeddable

@Embeddable
sealed class RestrictionJPAEntity {
    abstract fun toDomain(): Restriction

    object Private : RestrictionJPAEntity() {
        override fun toDomain() = Visibility.Private
    }

    object OnlyWomen : RestrictionJPAEntity() {
        override fun toDomain() = Visibility.OnlyWomen
    }

    companion object {
        fun from(constraint: Constraint): RestrictionJPAEntity {
            return when (constraint) {
                is Visibility.OnlyWomen -> OnlyWomen
                is Visibility.Private -> Private
                else -> throw IllegalArgumentException("Unknown constraint: $constraint")
            }
        }

        fun mapping(): Map<String, RestrictionJPAEntity> {
            return RestrictionJPAEntity::class.sealedSubclasses.associateBy(
                { it.simpleName!! },
                { it.objectInstance as RestrictionJPAEntity }
            )
        }
    }
}

@Converter
class RestrictionListConverter : SealedEntityListConverter<RestrictionJPAEntity>(
    RestrictionJPAEntity::class.java,
    RestrictionJPAEntity.mapping()
)