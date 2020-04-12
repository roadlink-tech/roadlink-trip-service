package com.fdt.tripservice.application.rest

import com.fdt.tripservice.application.dto.TripDto
import com.fdt.tripservice.domain.trip.Trip
import com.fdt.tripservice.domain.usecases.CreateTrip
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

const val createTripControllerUrl = "/trip-service"

@RestController
@RequestMapping(createTripControllerUrl)
class CreateTripController {

    /*
    @Autowired
    lateinit var createTrip: CreateTrip

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createTrip(@RequestBody body: TripDto, @RequestHeader(value = "Authorization") token: String)
            : ResponseEntity<Trip> {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createTrip.execute(token, body))
    }*/

}