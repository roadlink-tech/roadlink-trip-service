package com.fdt.tripservice.domain.trip

data class Section(val initial: Location, val final: Location, val passengers: MutableList<Long>) {

    fun joinPassenger(userId: Long) = passengers.add(userId)

    fun unjoinPassenger(userId: Long) = passengers.remove(userId)

    fun seatsOccupied() = passengers.size

    operator fun contains(userId: Long) = userId in passengers
}
