package com.roadlink.tripservice.config

import com.roadlink.tripservice.domain.common.IdGenerator
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class IdGeneratorConfig {
    @Singleton
    fun idGenerator(stubIdGenerator: StubIdGenerator): IdGenerator {
        return stubIdGenerator
    }
}

