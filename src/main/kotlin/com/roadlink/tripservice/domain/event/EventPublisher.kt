package com.roadlink.tripservice.domain.event

import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import kotlin.reflect.KClass

interface EventPublisher {
    fun suscribe(observer: Observer)
    fun publish(event: Event)
}


interface Command

interface CommandResponse
interface CommandBus {
    fun <C : Command, R : CommandResponse> publish(command: C): R
}

interface CommandHandler<C : Command, R : CommandResponse> {
    fun handle(command: C): R
}

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