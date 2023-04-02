package com.roadlink.tripservice.dev_tools.domain

import com.roadlink.tripservice.domain.trip.Address

interface Geoapify {
    fun addressByName(name: String): Address?
}