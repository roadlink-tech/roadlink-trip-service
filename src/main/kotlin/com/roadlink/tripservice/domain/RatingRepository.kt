package com.roadlink.tripservice.domain

interface RatingRepository {
    fun findByUserId(userId: String): Double?
}