package com.roadlink.tripservice.domain.user

interface UserTrustScoreRepository {
    fun findById(id: String): UserTrustScore
}

