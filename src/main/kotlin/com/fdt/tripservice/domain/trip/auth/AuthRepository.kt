package com.fdt.tripservice.domain.trip.auth

interface AuthRepository {
    fun findByToken(token: String): Auth
}