package com.roadlink.tripservice

import com.roadlink.tripservice.domain.Event
import com.roadlink.tripservice.domain.EventPublisher
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

class SpyEventPublisher(
    private val eventsPublished: MutableList<Event> = mutableListOf(),
) : EventPublisher {
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