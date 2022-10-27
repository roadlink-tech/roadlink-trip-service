package com.roadlink.tripservice.infrastructure.graphql

import graphql.kickstart.tools.GraphQLQueryResolver
import graphql.kickstart.tools.GraphQLResolver
import graphql.schema.DataFetchingEnvironment
import jakarta.inject.Singleton
import java.time.Instant

@Singleton
class SearchTripsGraphQLQuery : GraphQLQueryResolver {
    fun searchTrips(searchTripsInput: SearchTripsInput, environment: DataFetchingEnvironment): List<FooBarGraphQLType> {
        println(searchTripsInput)
        return listOf(
            FooBarGraphQLType(
                sections = listOf(
                    SectionGraphQLType(
                        departure = Location2(latitude = 184.12334F, longitude = -123.412F),
                        arrival = Location2(latitude = 184.12334F, longitude = -123.412F)
                    )
                )
            )
        )
    }
}

data class SearchTripsInput(val departure: LocationInput, val arrival: LocationInput, val at: Instant)

data class LocationInput(val latitude: Float, val longitude: Float)

data class TripPlanGraphQLType(val sections: List<SectionGraphQLType>)

data class SectionGraphQLType(val departure: Location2, val arrival: Location2)

data class Location2(val latitude: Float, val longitude: Float)

@Singleton
class TripPlanResolver : GraphQLResolver<TripPlanGraphQLType> {
    fun sections(tripPlan: TripPlanGraphQLType): List<SectionGraphQLType> {
        println("TripPlanResolver")
        return tripPlan.sections
    }
}

@Singleton
class SectionResolver : GraphQLResolver<TripPlanGraphQLType> {
    fun departure(section: SectionGraphQLType): Location2 {
        println("SectionResolver departure")
        return section.departure
    }

    fun arrival(section: SectionGraphQLType): Location2 {
        println("SectionResolver arrival")
        return section.arrival
    }
}

data class FooBarGraphQLType(val sections: List<SectionGraphQLType>)

@Singleton
class FooBarResolver : GraphQLResolver<FooBarGraphQLType> {
    fun sections(fooBar: FooBarGraphQLType): List<SectionGraphQLType> {
        println("FooBarResolver")
        return fooBar.sections
    }
}
