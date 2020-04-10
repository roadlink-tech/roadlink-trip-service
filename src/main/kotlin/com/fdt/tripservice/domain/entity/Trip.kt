package com.fdt.tripservice.domain.entity

class Trip(val path: List<Location>) {

    /*  A -> (B) -> C -> D -> (E) -> F

    A -> F : Se llama Trip
    A -> B : Se llama Section
    B -> D : se llama Subtrip      */

    fun hasSubtripSection(subtripSection: SubtripSection){
        // quiero saber si contiene los puntos y de manera ordenada B -> E
        // preguntarle a mis secciones de manera ordenada si contien B y luego E
    }

    fun hasSection(initial: Location, final: Location): Boolean{
        // ver q contiene ambos puntos de manera ordenada
        return path.indexOf(initial) < path.indexOf(final)
    }

    fun hasAvailableSeatAt(section: Section){

    }

    fun joinPassengerAt(userId: Long, section: Section){

    }
}

class SubtripSection(val initial: Location, val final: Location){
    // puntos ordenados no necesariamente inmediatamente correlativos
}

data class Section(val initial: Location, val final: Location, var capacity: Int){
    // puntos ordenados inmediatamente correlativos
}

data class Location(val latitude: Long, val longitude: Long){

}