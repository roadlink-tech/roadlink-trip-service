package com.roadlink.tripservice.dev_tools.infrastructure

import com.fasterxml.jackson.databind.ObjectMapper
import com.roadlink.tripservice.dev_tools.domain.Geoapify
import com.roadlink.tripservice.dev_tools.infrastructure.network.Get
import com.roadlink.tripservice.dev_tools.infrastructure.network.QueryParameter
import com.roadlink.tripservice.dev_tools.infrastructure.network.ReadRequest
import com.roadlink.tripservice.dev_tools.infrastructure.network.Response
import com.roadlink.tripservice.domain.Location
import com.roadlink.tripservice.domain.trip.Address

class HttpGeoapify(
    private val get: Get,
    private val scheme: String,
    private val host: String,
    private val port: Int,
    private val endpoint: String,
    private val apiKey: String,
    private val objectMapper: ObjectMapper,
) : Geoapify {
    override fun addressByName(name: String): Address? {
        val httpRequest = httpRequest(name)

        val response = get.dispatch(httpRequest)

        return address(response)
    }

    private fun httpRequest(name: String): ReadRequest =
        ReadRequest.Builder()
            .scheme(scheme)
            .host(host)
            .port(port)
            .endpoint(endpoint)
            .queryParameter(QueryParameter("apiKey", apiKey))
            .queryParameter(QueryParameter("text", name))
            .build()

    private fun address(response: Response): Address? =
        objectMapper.readTree(response.body).get("features").map { feature ->
            val properties = feature.get("properties")
            Address(
                location = Location(
                    latitude = properties.get("lat").asDouble(),
                    longitude = properties.get("lon").asDouble(),
                ),
                fullAddress = properties.get("formatted").asText(),
                street = properties.get("street").asText(),
                city = properties.get("city").asText(),
                country = properties.get("country").asText(),
                housenumber = properties.get("housenumber").asText(),
            )
        }.firstOrNull()
}
