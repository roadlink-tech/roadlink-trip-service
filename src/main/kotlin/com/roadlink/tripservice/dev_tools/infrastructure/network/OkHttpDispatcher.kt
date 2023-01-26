package com.roadlink.tripservice.dev_tools.infrastructure.network

import okhttp3.OkHttpClient
import okhttp3.Request
import java.time.Duration

class OkHttpDispatcher(
    readTimeout: Duration,
    connectionTimeout: Duration,
    writeTimeout: Duration
) {

    private val client = OkHttpClient().newBuilder()
        .readTimeout(readTimeout)
        .writeTimeout(writeTimeout)
        .connectTimeout(connectionTimeout)
        .build()

    fun execute(request: Request): Response {
        val response = client.newCall(request).execute()
        return Response(
            response.code,
            bodyString(response)
        )
    }

    private fun bodyString(response: okhttp3.Response): String {
        return response.body?.string() ?: ""
    }
}