package com.roadlink.tripservice.domain.user

import com.roadlink.tripservice.domain.driver_trip.Passenger

data class User(
    val id: String,
    val firstName: String,
    val lastName: String,
    val profilePhotoUrl: String
) {
    private fun fullName(): String {
        return "$firstName $lastName"
    }

    fun asPassengerWith(userTrustScore: UserTrustScore): Passenger {
        return Passenger(
            id = id,
            fullName = this.fullName(),
            score = userTrustScore.score,
            profilePhotoUrl = profilePhotoUrl,
            hasBeenRated = userTrustScore.hasAnyFeedbackReceived()
        )
    }
}

