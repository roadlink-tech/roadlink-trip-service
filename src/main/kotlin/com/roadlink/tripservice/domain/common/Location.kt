package com.roadlink.tripservice.domain.common

data class Location(val latitude: Double, val longitude: Double) {

    private var alias: String = "-"

    constructor(latitude: Double, longitude: Double, alias: String = "-") : this(latitude, longitude) {
        this.alias = alias
    }
}
