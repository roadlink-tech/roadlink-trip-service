package com.fdt.tripservice.domain.trip.auth

data class TokenInfo(val userId: Long, val roles: List<Role>)