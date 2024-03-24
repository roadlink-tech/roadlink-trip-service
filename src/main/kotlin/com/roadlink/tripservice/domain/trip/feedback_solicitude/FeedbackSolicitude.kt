package com.roadlink.tripservice.domain.trip.feedback_solicitude

import java.util.*

/**
 * This entity must be created when a trip finish.
 */
data class FeedbackSolicitude(
    val reviewerId: UUID,
    val receiverId: UUID,
    val tripLegId: UUID,
)