package com.roadlink.tripservice.config

import com.roadlink.tripservice.domain.common.IdGenerator
import com.roadlink.tripservice.infrastructure.UUIDGenerator
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class IdGeneratorConfig {
    @Singleton
    fun idGenerator(): IdGenerator {
        return UUIDGenerator()
    }
}
