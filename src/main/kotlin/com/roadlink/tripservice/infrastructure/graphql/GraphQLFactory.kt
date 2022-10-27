package com.roadlink.tripservice.infrastructure.graphql

import com.fasterxml.jackson.databind.ObjectMapper
import graphql.GraphQL
import graphql.kickstart.tools.PerFieldObjectMapperProvider
import graphql.kickstart.tools.SchemaParser
import graphql.kickstart.tools.SchemaParserOptions
import graphql.language.FieldDefinition
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class GraphQLFactory {

    @Bean
    @Singleton
    fun graphQL(
        objectMapper: ObjectMapper,
        searchTripsGraphQLQuery: SearchTripsGraphQLQuery,
        tripPlanResolver: TripPlanResolver,
        fooBarResolver: FooBarResolver,
    ): GraphQL {
        // Parse the schema.
        val builder = SchemaParser.newParser()
            .file("schema.graphqls")
            .options(SchemaParserOptions.newOptions()
                .objectMapperProvider(object : PerFieldObjectMapperProvider {
                    override fun provide(fieldDefinition: FieldDefinition): ObjectMapper {
                        return objectMapper
                    }
                })
                .build()
            )
            .resolvers(searchTripsGraphQLQuery, tripPlanResolver, fooBarResolver)
            .scalars(GraphQLInstant.instant)

        // Create the executable schema.
        val graphQLSchema = builder.build().makeExecutableSchema()

        // Return the GraphQL bean.
        return GraphQL.newGraphQL(graphQLSchema).build()
    }
}