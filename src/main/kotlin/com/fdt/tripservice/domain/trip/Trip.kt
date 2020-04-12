package com.fdt.tripservice.domain.trip

import java.time.LocalDate

class Trip(
        val departure: Location,
        val arrival: Location,
        val departureAt: LocalDate,
        val meetingPoints: List<Location>,
        val creatorId: Long)