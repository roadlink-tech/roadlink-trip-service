package com.roadlink.tripservice.domain

interface UserRepository {
    fun findFullNameById(userId: String): String?
}