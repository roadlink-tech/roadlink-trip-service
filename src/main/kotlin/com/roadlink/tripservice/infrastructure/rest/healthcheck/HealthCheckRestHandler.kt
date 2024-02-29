package com.roadlink.tripservice.infrastructure.rest.healthcheck

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces

@Controller("/health")
class HealthCheckRestHandler {
    @Get
    @Produces(MediaType.TEXT_PLAIN)
    fun handle(): String {
        return "ok"
    }
}