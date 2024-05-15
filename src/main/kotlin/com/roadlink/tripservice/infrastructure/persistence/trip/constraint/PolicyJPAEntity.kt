package com.roadlink.tripservice.infrastructure.persistence.trip.constraint

import com.roadlink.tripservice.domain.trip.constraint.Constraint
import com.roadlink.tripservice.domain.trip.constraint.Policy
import com.roadlink.tripservice.domain.trip.constraint.Rule
import jakarta.persistence.Converter
import jakarta.persistence.Embeddable

@Embeddable
sealed class PolicyJPAEntity {
    abstract fun toDomain(): Policy

    object PetAllowed : PolicyJPAEntity() {
        override fun toDomain() = Rule.PetAllowed
    }

    object NoSmoking : PolicyJPAEntity() {
        override fun toDomain() = Rule.NoSmoking
    }

    companion object {
        fun from(constraint: Constraint): PolicyJPAEntity {
            return when (constraint) {
                is Rule.NoSmoking -> NoSmoking
                is Rule.PetAllowed -> PetAllowed
                else -> throw IllegalArgumentException("Unknown constraint: $constraint")
            }
        }

        fun mapping(): Map<String, PolicyJPAEntity> {
            return PolicyJPAEntity::class.sealedSubclasses.associateBy(
                { it.simpleName!! },
                { it.objectInstance as PolicyJPAEntity }
            )
        }
    }
}

@Converter
class PolicyListConverter : SealedEntityListConverter<PolicyJPAEntity>(
    PolicyJPAEntity::class.java,
    PolicyJPAEntity.mapping()
)