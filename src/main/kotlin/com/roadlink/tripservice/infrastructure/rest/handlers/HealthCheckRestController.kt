package com.roadlink.tripservice.infrastructure.rest.handlers

import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Value
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import jakarta.inject.Singleton

@Controller("/health")
class HealthCheckRestController {
    @Get
    @Produces(MediaType.TEXT_PLAIN)
    fun handle(): String {
        return "ok"
    }
}

@Factory
class FooConfig {

    @Value("\${foo}")
    private lateinit var foo: String

    @Singleton
    fun foo() = Foo(foo)
}

class Foo(val value: String)

@Controller("/foo")
class FooRestController(private val foo: Foo) {
    @Get
    @Produces(MediaType.TEXT_PLAIN)
    fun handle(): String {
        return foo.value
    }
}
