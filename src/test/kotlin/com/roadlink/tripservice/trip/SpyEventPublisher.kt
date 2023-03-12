package com.roadlink.tripservice.trip

import com.roadlink.tripservice.domain.event.Event
import com.roadlink.tripservice.domain.event.EventPublisher
import com.roadlink.tripservice.domain.event.Observer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

class SpyEventPublisher(
    private val eventsPublished: MutableList<Event> = mutableListOf(),
) : EventPublisher {
    override fun suscribe(observer: Observer) {
        TODO("Not yet implemented")
    }

    override fun publish(event: Event) {
        eventsPublished.add(event)
    }

    fun verifyHasPublish(event: Event) {
        assertEquals(listOf(event), eventsPublished)
    }

    fun verifyNoEventHasBeenPublished() {
        assertTrue(eventsPublished.isEmpty())
    }
}