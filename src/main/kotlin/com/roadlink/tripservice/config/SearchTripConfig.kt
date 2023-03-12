package com.roadlink.tripservice.config

import com.roadlink.tripservice.domain.event.EventPublisher
import com.roadlink.tripservice.domain.event.InMemoryEventPublisher
import com.roadlink.tripservice.domain.time.DefaultTimeProvider
import com.roadlink.tripservice.domain.trip.observer.CreateTripObserver
import com.roadlink.tripservice.infrastructure.persistence.InMemoryTripRepository
import com.roadlink.tripservice.usecases.CreateTrip
import com.roadlink.tripservice.usecases.DefaultIdGenerator
import com.roadlink.tripservice.usecases.SearchTrip
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class SearchTripConfig {
    @Singleton
    fun searchTrip(bruteForceSearchEngine: com.roadlink.tripservice.domain.BruteForceSearchEngine): SearchTrip {
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