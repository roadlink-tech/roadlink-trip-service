package com.roadlink.tripservice.config

import com.roadlink.tripservice.domain.IdGenerator
import com.roadlink.tripservice.infrastructure.UUIDIdGenerator
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class IdGeneratorConfig {
    @Singleton
    fun idGenerator(): IdGenerator {
        return UUIDIdGenerator()
    }
}
