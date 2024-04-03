package com.roadlink.tripservice.dev_tools.config

import com.roadlink.tripservice.dev_tools.infrastructure.network.Post
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class PostConfig {

    @Singleton
    fun post(): Post {
        return Post()
    }
}