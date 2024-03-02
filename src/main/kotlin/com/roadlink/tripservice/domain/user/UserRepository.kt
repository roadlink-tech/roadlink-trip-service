package com.roadlink.tripservice.domain.user

interface UserRepository {
    fun findFullNameById(userId: String): String?
}