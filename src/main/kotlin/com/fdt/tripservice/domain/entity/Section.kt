package com.fdt.tripservice.domain.entity

data class Section(val initial: Location, val final: Location, var seatsOccupied: Int){
    // puntos ordenados inmediatamente correlativos
}