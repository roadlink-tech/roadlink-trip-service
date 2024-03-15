package com.roadlink.tripservice.domain.driver_trip

sealed class PassengerResult

data class Passenger(
    val id: String,
    val fullName: String,
    val score: Double,
    val hasBeenScored: Boolean,
    val profilePhotoUrl: String
) : PassengerResult()

data class PassengerNotExists(val id: String) : PassengerResult()
