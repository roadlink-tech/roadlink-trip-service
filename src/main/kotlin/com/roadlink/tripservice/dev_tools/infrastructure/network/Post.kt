package com.roadlink.tripservice.dev_tools.infrastructure.network

import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.time.Duration

class Post(
    readTimeout: Duration = Duration.ofSeconds(10),
    connectionTimeout: Duration = Duration.ofSeconds(10),
) : Component<WriteRequest> {

    private val okHttpDispatcher = OkHttpDispatcher(
        readTimeout = readTimeout,
        connectionTimeout = connectionTimeout,
        writeTimeout = readTimeout
    )

    override fun dispatch(request: WriteRequest): Response {
        val req = buildRequest(
            request.scheme,
            request.host,
            request.port,
            request.endpoint!!,
            request.body,
        )
        return okHttpDispatcher.execute(req)
    }

    private fun buildRequest(
        scheme: String,
        host: String,
        port: Int,
        endpoint: String,
        body: String,
    ): okhttp3.Request {
        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val requestBody = body.toRequestBody(contentType = mediaType)

        return okhttp3.Request.Builder()
            .post(requestBody)
            .url(
                HttpUrl.Builder()
                    .scheme(scheme)
                    .host(host)
                    .port(port)
                    .addPathSegments(endpoint)
                    .build()
            )
            .build()
    }
}