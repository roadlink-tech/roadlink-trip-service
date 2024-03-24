package com.roadlink.tripservice.domain.trip.feedback_solicitude

import java.util.*

data class FeedbackSolicitude(
    val reviewerId: UUID,
    val receiverId: UUID,
    val tripLegId: UUID,
)