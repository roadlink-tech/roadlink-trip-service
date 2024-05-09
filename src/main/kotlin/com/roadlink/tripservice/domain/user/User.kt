package com.roadlink.tripservice.domain.user

import com.roadlink.tripservice.domain.driver_trip.Passenger
import java.util.*

data class User(
    val id: String,
    val firstName: String,
    val lastName: String,
    val profilePhotoUrl: String,
    val gender: Gender = Gender.X,
    internal val friendsIds: Set<UUID> = emptySet()
) {
    private fun fullName(): String {
        return "$firstName $lastName"
    }

    fun isFriendOf(userId: UUID): Boolean {
        return this.friendsIds.contains(userId)
    }

    fun isAWomen(): Boolean {
        return this.gender == Gender.Female
    }

    fun toPassenger(userTrustScore: UserTrustScore): Passenger {
        return Passenger(
            id = id,
            fullName = this.fullName(),
            score = userTrustScore.score,
            profilePhotoUrl = profilePhotoUrl,
            hasBeenScored = userTrustScore.hasAnyFeedbackReceived()
        )
    }

    enum class Gender {
        X,
        Male,
        Female;

        companion object {
            fun fromString(value: String): Gender {
                return when (value.uppercase(Locale.getDefault())) {
                    "M" -> Male
                    "F" -> Female
                    else -> X
                }
            }
        }
    }
}

