package com.fdt.tripservice.domain.trip

interface TripRepository {

    fun save(trip: Trip): Trip
}