package com.roadlink.tripservice.trip.config

import com.roadlink.tripservice.trip.domain.*
import com.roadlink.tripservice.trip.infrastructure.persistence.InMemorySectionRepository
import com.roadlink.tripservice.trip.infrastructure.persistence.InMemoryTripRepository
import com.roadlink.tripservice.trip.usecases.CreateTrip
import com.roadlink.tripservice.trip.usecases.DefaultIdGenerator
import com.roadlink.tripservice.trip.usecases.SearchTrip
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class SearchTripConfig {
    @Singleton
    fun searchTrip(bruteForceSearchEngine: BruteForceSearchEngine): SearchTrip {
        return SearchTrip(
            searchEngine = bruteForceSearchEngine,
        )
    }

    @Singleton
    fun inMemoryTripRepository(): InMemoryTripRepository {
        return InMemoryTripRepository()
    }

    @Singleton
    fun createTripEventPublisher(): EventPublisher {
        val publisher = InMemoryEventPublisher()
        publisher.suscribe(CreateTripObserver())
        return publisher
    }
    @Singleton
    fun createTrip(
        inMemoryTripRepository: InMemoryTripRepository,
        createTripEventPublisher: EventPublisher
    ): CreateTrip {
        return CreateTrip(
            tripRepository = inMemoryTripRepository,
            idGenerator =  DefaultIdGenerator(),
            eventPublisher = createTripEventPublisher,
            timeProvider = DefaultTimeProvider()
        )
    }
}