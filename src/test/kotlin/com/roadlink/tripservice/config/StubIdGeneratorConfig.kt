package com.roadlink.tripservice.config

import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class StubIdGeneratorConfig {
    @Singleton
    fun stubIdGenerator(): StubIdGenerator {
        return StubIdGenerator()
    }
}