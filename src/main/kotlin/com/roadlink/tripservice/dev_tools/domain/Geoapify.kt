package com.roadlink.tripservice.dev_tools.domain

interface Geoapify {
    fun geocoding(name: String): GeoapifyPoint?
}