package com.roadlink.tripservice.domain

sealed class PassengerResult

data class Passenger(
    val id: String,
    val fullName: String,
    val rating: RatingResult,
) : PassengerResult()

data class PassengerNotExists(val id: String) : PassengerResult()
