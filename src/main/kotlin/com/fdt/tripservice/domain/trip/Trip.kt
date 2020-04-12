package com.fdt.tripservice.domain.trip

import java.time.LocalDate

class Trip(
        var id: Long? = null,
        val departure: Location,
        val arrival: Location,
        val departureAt: LocalDate,
        val meetingPoints: List<Location>,
        val creatorId: Long) {

    fun containsPassenger(userId: Long): Boolean {
        //TODO
        return false
    }

    fun hasSection(tripSection: Long): Boolean {
        //TODO
        return true
    }

    fun hasAvailableSeatAt(section: Long): Boolean {
        //TODO
        return true
    }

    fun joinPassengerAt(userId: Long, section: Long) {
        //TODO
    }
}