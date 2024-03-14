package com.roadlink.tripservice.infrastructure.persistence.user

import com.roadlink.tripservice.domain.user.User
import com.roadlink.tripservice.domain.user.UserRepository
import java.util.UUID

class FixedUserRepository : UserRepository {

    /*
    private val users: Map<String, String> = mapOf(
        "JOHN" to "John Krasinski",
        "JENNA" to "Jenna Fischer",
        "BJNOVAK" to "B.J.Novak",
        "STEVE" to "Steve Carell",
        "PAINN" to "Painn Wilson",
    )*/

    private val users: Map<String, User> = mapOf(
        "JOHN" to User(
            id = UUID.randomUUID().toString(),
            firstName = "John",
            lastName = "Krasinski",
            profilePhotoUrl = ""
        ),
        "JENNA" to User(
            id = UUID.randomUUID().toString(),
            firstName = "Jenna",
            lastName = "Fischer",
            profilePhotoUrl = ""
        ),
        "BJNOVAK" to User(
            id = UUID.randomUUID().toString(),
            firstName = "B.J.",
            lastName = "Novak",
            profilePhotoUrl = ""
        ),
        "STEVE" to User(
            id = UUID.randomUUID().toString(),
            firstName = "Steve",
            lastName = "Carell",
            profilePhotoUrl = ""
        ),
        "PAINN" to User(
            id = UUID.randomUUID().toString(),
            firstName = "Painn",
            lastName = "Wilson",
            profilePhotoUrl = ""
        ),
    )


    override fun findByUserId(userId: String): User? {
        return users[userId]
    }

}