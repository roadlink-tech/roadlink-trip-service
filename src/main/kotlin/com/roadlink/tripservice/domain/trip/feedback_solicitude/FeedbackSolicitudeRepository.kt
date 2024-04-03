package com.roadlink.tripservice.domain.trip.feedback_solicitude

interface FeedbackSolicitudeRepository {
    fun insert(feedbackSolicitude: FeedbackSolicitude)
}