package com.roadlink.tripservice.infrastructure.persistence

import com.roadlink.tripservice.domain.UserRepository

class FixedUserRepository : UserRepository {

    private val users: Map<String, String> = mapOf(
        "JOHN" to "John Krasinski",
        "JENNA" to "Jenna Fischer",
        "BJNOVAK" to "B.J.Novak",
        "STEVE" to "Steve Carell",
        "PAINN" to "Painn Wilson",
    )

    override fun findFullNameById(userId: String): String? =
        users[userId]

}