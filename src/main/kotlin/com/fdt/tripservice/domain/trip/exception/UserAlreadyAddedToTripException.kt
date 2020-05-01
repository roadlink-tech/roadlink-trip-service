package com.fdt.tripservice.domain.trip.exception

import java.lang.RuntimeException

class UserAlreadyAddedToTripException(message: String) : RuntimeException(message)