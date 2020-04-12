package com.fdt.tripservice.domain.trip.auth

interface TokenService {

    fun getTokenInfo(token: String): TokenInfo
}