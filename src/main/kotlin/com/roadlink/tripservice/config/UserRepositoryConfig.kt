package com.roadlink.tripservice.config

import com.roadlink.tripservice.domain.user.UserRepository
import com.roadlink.tripservice.infrastructure.persistence.user.FixedUserRepository
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class UserRepositoryConfig {
    @Singleton
    fun userRepository(): UserRepository {
        return FixedUserRepository()
    }
}