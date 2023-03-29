package com.roadlink.tripservice.domain.trip.events

import com.roadlink.tripservice.domain.trip.events.command_responses.CommandResponse
import com.roadlink.tripservice.domain.trip.events.commands.Command
import com.roadlink.tripservice.domain.trip.events.handlers.CommandHandler
import kotlin.reflect.KClass

class SimpleCommandBus : CommandBus {

    private val handlers = mutableMapOf<Class<out Command>, CommandHandler<Command, CommandResponse>>()

    override fun <C : Command, R : CommandResponse> publish(command: C): R {
        val handler = handlers[command::class.java] as? CommandHandler<C, R>
            ?: throw IllegalStateException("No handler registered for command of type ${command::class.java}")
        return handler.handle(command)
    }

    fun registerHandler(handler: CommandHandler<out Command, out CommandResponse>) {
        val commandClass = handler::class.supertypes
            .firstOrNull { it.classifier == CommandHandler::class }?.arguments?.first()?.type?.classifier as? KClass<out Command>
            ?: throw IllegalArgumentException("Handler class does not implement CommandHandler")
        handlers[commandClass.java] = handler as CommandHandler<Command, CommandResponse>
    }
}