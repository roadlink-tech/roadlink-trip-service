package com.roadlink.tripservice.infrastructure.rest.responses

enum class RatingResultResponseType {
    RATED,
    NOT_BEEN_RATED,
}

sealed class RatingResultResponse(open val type: RatingResultResponseType)

data class NotBeenRatedResponse(
    override val type: RatingResultResponseType = RatingResultResponseType.NOT_BEEN_RATED,
) : RatingResultResponse(type)

data class RatedResponse(
    override val type: RatingResultResponseType = RatingResultResponseType.RATED,
    val rating: Double,
) : RatingResultResponse(type)
