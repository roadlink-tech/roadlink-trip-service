package com.fdt.tripservice.domain.trip

import com.fdt.tripservice.application.dto.TripDto

open class TripFactory {

    fun create(tripDto: TripDto): Trip {
        return tripDto.toTrip()
    }
}