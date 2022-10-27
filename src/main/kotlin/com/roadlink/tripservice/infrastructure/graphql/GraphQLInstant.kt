package com.roadlink.tripservice.infrastructure.graphql

import graphql.language.StringValue
import graphql.schema.Coercing
import graphql.schema.CoercingSerializeException
import graphql.schema.GraphQLScalarType
import java.time.Instant
import java.time.format.DateTimeParseException

object GraphQLInstant {
    val instant: GraphQLScalarType = GraphQLScalarType
        .newScalar()
        .name("Instant2")
        .description("ISO-8601 representation of an instant")
        .coercing(object : Coercing<Instant, String> {
            override fun serialize(dataFetcherResult: Any): String {
                return when (dataFetcherResult) {
                    is Instant -> dataFetcherResult.toString()
                    else -> throw CoercingSerializeException("Expected a 'java.time.Instant' but was ${dataFetcherResult::class.java} ")
                }
            }

            override fun parseValue(input: Any): Instant {
                return when (input) {
                    is String ->
                        try {
                            Instant.parse(input)
                        } catch (e: DateTimeParseException) {
                            throw CoercingSerializeException("Unable to parse value to 'java.time.Instant' because of: ${e.message}")
                        }
                    else ->
                        throw CoercingSerializeException("Expected a 'java.lang.String' but was ${input::class.java} ")
                }
            }

            override fun parseLiteral(input: Any): Instant {
                return when (input) {
                    is StringValue -> Instant.parse(input.value)
                    else -> throw CoercingSerializeException("Expected a 'java.lang.String' but was ${input::class.java} ")
                }
            }

        })
        .build()
}
