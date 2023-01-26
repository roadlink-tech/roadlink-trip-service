package com.roadlink.tripservice.trip.domain

interface IdGenerator {
    fun id(): String
}