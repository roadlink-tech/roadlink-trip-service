package com.fdt.tripservice.domain.entity

class Trip(val path: List<Location>, val capacity: Int) {

    private var sections = Sections(path)

    /*  A -> (B) -> C -> D -> (E) -> F

    A -> F : Se llama Trip, que tiene todas las locations y sections individuales
    A -> B : Se llama Section
    B -> D : se llama Subtrip      */

    fun hasSubtripSection(initial: Location, final: Location): Boolean{
        return existLocationInTrip(initial) && existLocationInTrip(final)
                && areInOrder(initial,final)
    }

    private fun existLocationInTrip(location: Location): Boolean{
        // TODO see complexity
        return (path.contains(location))
    }

    private fun areInOrder(previous: Location, posterior: Location): Boolean{
        return path.indexOf(previous) < path.indexOf(posterior)
    }

    fun hasAvailableSeatAt(subtripSection: SubtripSection): Boolean{
        if (hasSubtripSection(subtripSection.initial, subtripSection.final)){
            for (section in sections.getSectionsFrom(subtripSection)){
                if (section.seatsOccupied >= capacity) return false
            }
        }
        return true
    }

    fun joinPassengerAt(userId: Long, subtripSection: SubtripSection){
        // ya se que hay espacio
        // me traigo las sectiones y me agrego
        sections.getSectionsFrom(subtripSection).map { it.seatsOccupied += 1 }
    }
}

class SubtripSection(val initial: Location, val final: Location){
    // puntos ordenados no necesariamente inmediatamente correlativos
}

data class Section(val initial: Location, val final: Location, var seatsOccupied: Int){
    // puntos ordenados inmediatamente correlativos
}

data class Location(val latitude: Long, val longitude: Long){

}