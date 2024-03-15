package com.roadlink.tripservice.usecases.user

import com.roadlink.tripservice.domain.user.UserTrustScore

object UserTrustScoreFactory {

    fun common(
        score: Double = 5.0,
        feedbacks: UserTrustScore.Feedbacks = UserTrustScore.Feedbacks(given = 1, received = 1)
    ): UserTrustScore {
        return UserTrustScore(
            score = score,
            feedbacks = feedbacks
        )
    }
}