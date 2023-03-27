package com.roadlink.tripservice.trip

import com.roadlink.tripservice.domain.event.*
import kotlin.reflect.KClass

class SpyCreateTripHandler : CommandHandler<TripCreatedCommandV2, TripCreatedCommandResponseV2> {
    override fun handle(command: TripCreatedCommandV2): TripCreatedCommandResponseV2 {
        return TripCreatedCommandResponseV2(command.trip)
    }
}

class SpyCommandBus : CommandBus {
    val handlers = mutableMapOf<Class<out Command>, CommandHandler<Command, CommandResponse>>()

    internal val publishedCommands = mutableListOf<Command>()

    init {
        registerHandler(SpyCreateTripHandler())
    }

    override fun <C : Command, R : CommandResponse> publish(command: C): R {
        this.publishedCommands.add(command)
        val handler = handlers[command::class.java] as? CommandHandler<C, R>
            ?: throw IllegalStateException("No handler registered for command of type ${command::class.java}")
        return handler.handle(command)
    }

    fun clear() {
        this.handlers.clear()
        this.publishedCommands.clear()
    }

    fun registerHandler(handler: CommandHandler<out Command, out CommandResponse>) {
        val commandClass = handler::class.supertypes
            .firstOrNull { it.classifier == CommandHandler::class }?.arguments?.first()?.type?.classifier as? KClass<out Command>
            ?: throw IllegalArgumentException("Handler class does not implement CommandHandler")
        handlers[commandClass.java] = handler as CommandHandler<Command, CommandResponse>
    }


}