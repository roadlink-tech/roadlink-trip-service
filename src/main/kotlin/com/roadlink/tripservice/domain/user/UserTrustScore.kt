package com.roadlink.tripservice.domain.user

data class UserTrustScore(
    val score: Double,
    val feedbacks: Feedbacks
) {
    fun hasAnyFeedbackReceived(): Boolean {
        return feedbacks.received > 0
    }

    data class Feedbacks(val given: Int, val received: Int)
}