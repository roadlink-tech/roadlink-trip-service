package com.roadlink.tripservice.infrastructure.persistence

import com.roadlink.tripservice.domain.RatingRepository

class FixedRatingRepository : RatingRepository {

    private val ratings: Map<String, Double> = mapOf(
        "JOHN" to 1.3,
        "JENNA" to 2.0,
        "BJNOVAK" to 2.7,
        "STEVE" to 4.1,
    )

    override fun findByUserId(userId: String): Double? =
        ratings[userId]

}