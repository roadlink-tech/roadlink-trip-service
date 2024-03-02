package com.roadlink.tripservice.config

import com.roadlink.tripservice.domain.trip.events.CommandBus
import com.roadlink.tripservice.domain.trip.events.command_responses.CommandResponse
import com.roadlink.tripservice.domain.trip.events.command_responses.TripCreatedCommandResponse
import com.roadlink.tripservice.domain.trip.events.commands.Command
import com.roadlink.tripservice.domain.trip.events.commands.TripCreatedCommand
import com.roadlink.tripservice.domain.trip.events.handlers.CommandHandler
import kotlin.reflect.KClass

class StubCreateTripHandler : CommandHandler<TripCreatedCommand, TripCreatedCommandResponse> {
    override fun handle(command: TripCreatedCommand): TripCreatedCommandResponse {
        return TripCreatedCommandResponse(command.trip)
    }
}

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