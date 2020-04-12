package com.fdt.tripservice.domain.entity

class Trip(path: List<Location>, private val capacity: Int) {

    private var sections = Sections(path)

    /*
    A -> (B) -> C -> D -> (E) -> F

    A -> F : Se llama Trip, que tiene todas las locations y sections individuales
    A -> B : Se llama Section
    B -> D : se llama Subtrip
    */

    fun hasSubtrip(subtrip: Subtrip) = subtrip in sections

    fun hasAvailableSeatAt(subtrip: Subtrip): Boolean {
        for (section in sections[subtrip]) {
            if (section.seatsOccupied >= capacity) return false
        }
        return true
    }

    fun joinPassengerAt(userId: Long, subtrip: Subtrip) {
        sections[subtrip].map { it.seatsOccupied += 1 }
    }
}
