package com.roadlink.tripservice.domain

sealed class RatingResult

object NotBeenRated : RatingResult()

data class Rated(val rating: Double) : RatingResult()
