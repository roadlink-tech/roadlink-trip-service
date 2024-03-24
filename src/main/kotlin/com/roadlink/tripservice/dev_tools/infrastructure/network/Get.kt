package com.roadlink.tripservice.dev_tools.infrastructure.network

import okhttp3.Headers.Companion.toHeaders
import okhttp3.HttpUrl
import java.time.Duration

class Get(
    readTimeout: Duration = Duration.ofSeconds(10),
    connectionTimeout: Duration = Duration.ofSeconds(10),
) : Component {
    private val okHttpDispatcher = OkHttpDispatcher(
        readTimeout = readTimeout,
        connectionTimeout = connectionTimeout,
        writeTimeout = readTimeout
    )

    override fun dispatch(request: Request): Response {
        val req = buildRequest(
            request.scheme,
            request.host,
            request.port,
            request.endpoint!!,
            request.queryParameters,
            request.headers,
        )
        return okHttpDispatcher.execute(req)
    }

    private fun buildRequest(
        scheme: String,
        host: String,
        port: Int,
        endpoint: String,
        queryParameters: List<QueryParameter>,
        headers: List<Header>,
    ): okhttp3.Request {
        return okhttp3.Request.Builder()
            .get()
            .url(HttpUrl.Builder()
                .scheme(scheme)
                .host(host)
                .port(port)
                .addPathSegments(endpoint)
                .apply {
                    queryParameters.forEach {
                        addQueryParameter(it.name, it.value)
                    }
                }
                .build())
            .headers(headers.associate {
                Pair(
                    it.key,
                    it.value
                )
            }.toHeaders())
            .build()
    }
}