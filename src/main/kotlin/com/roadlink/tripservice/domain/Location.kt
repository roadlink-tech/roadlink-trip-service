package com.roadlink.tripservice.domain

data class Location(val latitude: Double, val longitude: Double) {

    var alias: String = "-"

    constructor(latitude: Double, longitude: Double, alias: String = "-") : this(latitude, longitude) {
        this.alias = alias
    }
}
