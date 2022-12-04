package com.roadlink.tripservice.domain

import java.time.Instant

object InstantFactory {
    fun october15_7hs(): Instant = Instant.parse("2022-10-15T07:00:00Z")
    fun october15_12hs(): Instant = Instant.parse("2022-10-15T12:00:00Z")
    fun october15_13hs(): Instant = Instant.parse("2022-10-15T13:00:00Z")
    fun october15_15hs(): Instant = Instant.parse("2022-10-15T15:00:00Z")
    fun october15_17hs(): Instant = Instant.parse("2022-10-15T17:00:00Z")
    fun october15_17_30hs(): Instant = Instant.parse("2022-10-15T17:30:00Z")
    fun october15_18hs(): Instant = Instant.parse("2022-10-15T18:00:00Z")
    fun october15_22hs(): Instant = Instant.parse("2022-10-15T22:00:00Z")
}