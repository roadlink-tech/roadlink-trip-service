package com.roadlink.tripservice.config

import com.roadlink.tripservice.domain.common.events.Command
import com.roadlink.tripservice.domain.common.events.CommandBus
import com.roadlink.tripservice.domain.common.events.CommandHandler
import com.roadlink.tripservice.domain.common.events.CommandResponse
import com.roadlink.tripservice.domain.trip.events.TripCreatedEvent
import com.roadlink.tripservice.domain.trip.events.TripCreatedEventResponse
import kotlin.reflect.KClass

class StubCreateTripHandler : CommandHandler<TripCreatedEvent, TripCreatedEventResponse> {
    override fun handle(command: TripCreatedEvent): TripCreatedEventResponse {
        return TripCreatedEventResponse(command.trip)
    }
}

// TODO check if this spy class is necessary
class SpyCommandBus : CommandBus {
    val handlers = mutableMapOf<Class<out Command>, CommandHandler<Command, CommandResponse>>()

    internal val publishedCommands = mutableListOf<Command>()

    init {
        registerHandler(StubCreateTripHandler())
    }

    override fun <C : Command, R : CommandResponse> publish(command: C): R {
        this.publishedCommands.add(command)
        val handler = handlers[command::class.java] as? CommandHandler<C, R>
            ?: throw IllegalStateException("No handler registered for command of type ${command::class.java}")
        return handler.handle(command)
    }

    fun clear() {
        this.publishedCommands.clear()
    }

    fun registerHandler(handler: CommandHandler<out Command, out CommandResponse>) {
        val commandClass = handler::class.supertypes
            .firstOrNull { it.classifier == CommandHandler::class }?.arguments?.first()?.type?.classifier as? KClass<out Command>
            ?: throw IllegalArgumentException("Handler class does not implement CommandHandler")
        handlers[commandClass.java] = handler as CommandHandler<Command, CommandResponse>
    }

}