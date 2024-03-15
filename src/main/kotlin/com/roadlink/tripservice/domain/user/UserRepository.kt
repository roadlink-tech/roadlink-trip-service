package com.roadlink.tripservice.domain.user

interface UserRepository {
    fun findByUserId(userId: String): User?
}

