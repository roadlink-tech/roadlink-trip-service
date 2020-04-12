package com.fdt.tripservice.domain.trip

data class Section(val initial: Location, val final: Location, var seatsOccupied: Int)
