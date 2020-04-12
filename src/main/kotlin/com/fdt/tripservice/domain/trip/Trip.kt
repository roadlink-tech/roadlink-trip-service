package com.fdt.tripservice.domain.trip

import java.time.LocalDate

class Trip(
        var id: Long? = null,
        val departure: Location,
        val arrival: Location,
        val departureAt: LocalDate,
        val meetingPoints: List<Location>,
        val creatorId: Long,
        val capacity: Int
) {

    private var sections = Sections(listOf<Location>() + departure + meetingPoints + arrival)

    /*
       A -> (B) -> C -> D -> (E) -> F
       A -> F : Se llama Trip, que tiene todas las locations y sections individuales
       A -> B : Se llama Section
       B -> D : se llama Subtrip
   */

    fun containsPassenger(userId: Long): Boolean {
        //TODO
        return false
    }

    fun hasSubtrip(subtrip: Subtrip) = subtrip in sections

    fun hasAvailableSeatAt(subtrip: Subtrip): Boolean {
        for (section in sections[subtrip]) {
            if (section.seatsOccupied >= capacity) return false
        }
        return true
    }

    fun joinPassengerAt(userId: Long, subtrip: Subtrip) {
        sections[subtrip].map { it.seatsOccupied += 1 }    }
}