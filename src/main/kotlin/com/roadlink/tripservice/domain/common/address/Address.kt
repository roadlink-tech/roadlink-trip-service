package com.roadlink.tripservice.domain.common.address

import com.roadlink.tripservice.domain.common.Location

data class Address(
    val location: Location,
    val fullAddress: String,
    val street: String,
    val city: String,
    val country: String,
    val houseNumber: String,
)
