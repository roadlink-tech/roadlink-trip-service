package com.roadlink.tripservice.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.roadlink.tripservice.dev_tools.config.HttpGeoapifyConfig
import com.roadlink.tripservice.dev_tools.infrastructure.network.Get
import com.roadlink.tripservice.domain.user.UserRepository
import com.roadlink.tripservice.infrastructure.persistence.user.FixedUserRepository
import com.roadlink.tripservice.infrastructure.remote.HttpUserRepository
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Value
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory

@Factory
class UserRepositoryConfig {

    companion object {
        private val logger = LoggerFactory.getLogger(UserRepositoryConfig::class.java)
    }

    @Value("\${http-get-user-by-id.scheme}")
    private lateinit var getUserByIdScheme: String

    @Value("\${http-get-user-by-id.host}")
    private lateinit var getUserByIdHost: String

    @Value("\${http-get-user-by-id.port}")
    private var getUserByIdPort: Int? = null

    @Value("\${http-get-user-by-id.endpoint}")
    private lateinit var getUserByIdEndpoint: String

    @Singleton
    fun userRepository(get: Get, objectMapper: ObjectMapper): UserRepository {
        logger.debug("Initializing http get user by id with scheme: {}, host: {}, port: {}, endpoint: {}", getUserByIdScheme, getUserByIdHost, getUserByIdPort, getUserByIdEndpoint)

        return HttpUserRepository(
            get = get,
            scheme = getUserByIdScheme,
            host = getUserByIdHost,
            port = getUserByIdPort!!,
            endpoint = getUserByIdEndpoint,
            objectMapper = objectMapper,
        )
    }
}