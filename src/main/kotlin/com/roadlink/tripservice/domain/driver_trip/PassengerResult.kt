package com.roadlink.tripservice.domain.driver_trip

import com.roadlink.tripservice.domain.RatingResult

sealed class PassengerResult

data class Passenger(
    val id: String,
    val fullName: String,
    val rating: RatingResult,
) : PassengerResult()

data class PassengerNotExists(val id: String) : PassengerResult()
