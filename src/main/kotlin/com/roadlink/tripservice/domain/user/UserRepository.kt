package com.roadlink.tripservice.domain.user

interface UserRepository {
    fun findByUserId(id: String): User?
}

