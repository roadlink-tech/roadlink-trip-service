package com.roadlink.tripservice.dev_tools.infrastructure.network

interface Request {
    val scheme: String
    val host: String
    val port: Int
    val endpoint: String?
    val headers: List<Header>
    fun url(): String
}

data class Header(
    val key: String,
    val value: String
)

data class QueryParameter(
    val name: String,
    val value: String,
)

data class ReadRequest private constructor(
    override val scheme: String,
    override val host: String,
    override val port: Int,
    override val endpoint: String?,
    override val headers: List<Header> = emptyList(),
    val queryParameters: List<QueryParameter> = emptyList(),
) : Request {
    data class Builder(
        var scheme: String? = null,
        var host: String? = null,
        var port: Int? = null,
        var endpoint: String? = null,
        var queryParameters: MutableList<QueryParameter> = mutableListOf(),
        var headers: MutableList<Header> = mutableListOf(),
    ) {

        fun header(header: Header): Builder {
            return apply { this.headers.add(header) }
        }

        fun endpoint(endpoint: String): Builder {
            return apply { this.endpoint = endpoint }
        }

        fun host(host: String): Builder {
            return apply { this.host = host }
        }

        fun queryParameter(queryParameter: QueryParameter): Builder {
            return apply { this.queryParameters.add(queryParameter) }
        }

        fun scheme(scheme: String): Builder {
            return apply { this.scheme = scheme }
        }

        fun port(port: Int): Builder {
            return apply { this.port = port }
        }

        fun build(): ReadRequest {
            return host?.let {
                ReadRequest(
                    scheme = scheme!!,
                    host = host!!,
                    port = port!!,
                    endpoint = endpoint,
                    queryParameters = this.queryParameters,
                    headers = this.headers,
                )
            } ?: throw EmptyUrlException("Url must not be not null")
        }
    }

    override fun url(): String {
        return "$scheme://$host$endpoint"
    }
}

data class WriteRequest private constructor(
    override val scheme: String,
    override val host: String,
    override val port: Int,
    override val endpoint: String?,
    override val headers: List<Header> = emptyList(),
    val body: String,
) : Request {

    data class Builder(
        var scheme: String? = null,
        var host: String? = null,
        var port: Int? = null,
        var endpoint: String? = null,
        var body: String? = null,
    ) {

        fun endpoint(endpoint: String) = apply {
            this.endpoint = endpoint
        }

        fun host(host: String) = apply {
            this.host = host
        }

        fun scheme(scheme: String) = apply {
            this.scheme = scheme
        }

        fun port(port: Int) = apply {
            this.port = port
        }

        fun body(body: String) = apply {
            this.body = body
        }

        fun build(): WriteRequest {
            return host?.let {
                WriteRequest(
                    scheme = scheme!!,
                    host = host!!,
                    port = port!!,
                    endpoint = endpoint,
                    body = body!!,
                )
            } ?: throw EmptyUrlException("Url must not be not null")
        }
    }

    override fun url(): String {
        return "$scheme://$host$endpoint"
    }
}

class EmptyUrlException(message: String) : RuntimeException(message)