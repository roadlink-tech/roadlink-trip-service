package com.roadlink.tripservice.config

import com.roadlink.tripservice.domain.time.DefaultTimeProvider
import com.roadlink.tripservice.domain.time.TimeProvider
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class TimeProviderConfig {
    @Singleton
    fun timeProvider(): TimeProvider {
        return DefaultTimeProvider()
    }
}