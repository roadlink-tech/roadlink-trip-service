package com.roadlink.tripservice.dev_tools.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.roadlink.tripservice.dev_tools.infrastructure.HttpGeoapify
import com.roadlink.tripservice.dev_tools.infrastructure.network.Get
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Value
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory

@Factory
class HttpGeoapifyConfig {

    companion object {
        private val logger = LoggerFactory.getLogger(HttpGeoapifyConfig::class.java)
    }

    @Value("\${geoapify.http.scheme}")
    private lateinit var geoapifyScheme: String

    @Value("\${geoapify.http.host}")
    private lateinit var geoapifyHost: String

    @Value("\${geoapify.http.port}")
    private var geoapifyPort: Int? = null

    @Value("\${geoapify.http.endpoint}")
    private lateinit var geoapifyEndpoint: String

    @Value("\${geoapify.apiKey}")
    private lateinit var geoapifyApiKey: String

    @Singleton
    fun httpGeoapify(get: Get, objectMapper: ObjectMapper): HttpGeoapify {
        logger.debug("Initializing http geoapify with scheme: {}, host: {}, port: {}, endpoint: {}", geoapifyScheme, geoapifyHost, geoapifyPort, geoapifyEndpoint)

        return HttpGeoapify(
            get = get,
            scheme = geoapifyScheme,
            host = geoapifyHost,
            port = geoapifyPort!!,
            endpoint = geoapifyEndpoint,
            apiKey = geoapifyApiKey,
            objectMapper = objectMapper,
        )
    }
}