package com.roadlink.tripservice.infrastructure.persistence.trip_plan

import com.roadlink.tripservice.domain.trip_plan.TripPlan
import com.roadlink.tripservice.domain.trip_plan.TripPlanRepository
import io.micronaut.transaction.TransactionOperations
import jakarta.persistence.EntityManager
import org.hibernate.Session

class MySQLTripPlanRepository(
    private val entityManager: EntityManager,
    private val transactionManager: TransactionOperations<Session>,
) : TripPlanRepository {
    override fun insert(tripPlan: TripPlan): TripPlan {
        transactionManager.executeWrite {
            entityManager.persist(TripPlanJPAEntity.from(tripPlan))
        }
        return tripPlan
    }
}