package com.roadlink.tripservice.usecases.user

import com.roadlink.tripservice.domain.user.User
import java.util.*

object UserFactory {

    fun common(
        id: UUID = UUID.randomUUID(),
        firstName: String = "John",
        lastName: String = "Krasinski",
        profilePhotoUrl: String = "http//profile.photo.com",
        friendsIds: Set<UUID> = setOf(),
        gender: User.Gender = User.Gender.None
    ): User {
        return User(
            id = id.toString(),
            firstName = firstName,
            lastName = lastName,
            profilePhotoUrl = profilePhotoUrl,
            friendsIds = friendsIds,
            gender = gender
        )
    }
}