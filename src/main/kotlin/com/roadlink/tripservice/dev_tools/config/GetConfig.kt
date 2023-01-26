package com.roadlink.tripservice.dev_tools.config

import com.roadlink.tripservice.dev_tools.infrastructure.network.Get
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class GetConfig {
    @Singleton
    fun get(): Get {
        return Get()
    }
}
