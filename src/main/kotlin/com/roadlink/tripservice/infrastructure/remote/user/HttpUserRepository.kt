package com.roadlink.tripservice.infrastructure.remote.user

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.roadlink.tripservice.dev_tools.infrastructure.network.Get
import com.roadlink.tripservice.dev_tools.infrastructure.network.ReadRequest
import com.roadlink.tripservice.domain.user.User
import com.roadlink.tripservice.domain.user.UserRepository
import java.util.UUID

class HttpUserRepository(
    private val get: Get,
    private val scheme: String,
    private val host: String,
    private val port: Int,
    private val endpoint: String,
    private val objectMapper: ObjectMapper,
) : UserRepository {

    override fun findByUserId(userId: String): User? {
        val httpRequest = ReadRequest.Builder()
            .scheme(scheme)
            .host(host)
            .port(port)
            .endpoint(endpoint.replace("{userId}", userId))
            .build()

        val response = get.dispatch(httpRequest)

        return when {
            response.isSucceeded() -> {
                val userResponse =
                    objectMapper.readValue(response.body, UserCoreResponse::class.java)
                userResponse.toDomain()
            }

            response.statusCode == 404 -> {
                null
            }

            else -> {
                throw RuntimeException("Unknown error")
            }
        }
    }
}

data class UserCoreResponse(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("first_name")
    val firstName: String,
    @JsonProperty("last_name")
    val lastName: String,
    @JsonProperty("email")
    val email: String,
    @JsonProperty("user_name")
    val userName: String,
    @JsonProperty("profile_photo_url")
    val profilePhotoUrl: String,
    @JsonProperty("gender")
    val gender: String,
    @JsonProperty("friends")
    val friends: Set<UUID>
) {

    fun toDomain(): User {
        return User(
            id = id,
            firstName = firstName,
            lastName = lastName,
            profilePhotoUrl = profilePhotoUrl,
            friendsIds = friends,
            gender = User.Gender.fromString(gender)
        )
    }
}
