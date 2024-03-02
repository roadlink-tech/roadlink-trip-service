package com.roadlink.tripservice.config

import com.roadlink.tripservice.domain.common.utils.time.DefaultTimeProvider
import com.roadlink.tripservice.domain.common.utils.time.TimeProvider
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class TimeProviderConfig {
    @Singleton
    fun timeProvider(): TimeProvider {
        return DefaultTimeProvider()
    }
}