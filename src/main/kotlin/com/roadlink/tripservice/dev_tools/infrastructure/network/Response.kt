package com.roadlink.tripservice.dev_tools.infrastructure.network

import java.time.LocalDateTime

class Response(
    val statusCode: Int,
    val body: String,
    val createdDate: LocalDateTime = LocalDateTime.now()
) {
    fun isSucceeded(): Boolean {
        return statusCode in 200..299
    }

    fun isOk(): Boolean = statusCode == 200
}