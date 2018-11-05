package com.raranda.kcqrs.command.infrastructure

import com.raranda.kcqrs.command.domain.Command
import com.raranda.kcqrs.command.domain.CommandBus
import com.raranda.kcqrs.command.domain.CommandHandler
import com.raranda.kcqrs.shared.NoRegisteredHandlerException
import kotlin.reflect.KClass

class SimpleCommandBus : CommandBus {
    private val handlers: MutableMap<KClass<*>, CommandHandler<*, *>> = mutableMapOf()

    override fun <C : Command> register(handler: CommandHandler<C, *>) {
        handlers[handler.commandClass()] = handler
    }

    @Suppress("UNCHECKED_CAST")
    override fun <R> handle(command: Command): R =
            (handlers[command::class] as? CommandHandler<Command, R>)?.on(command)
            ?: throw NoRegisteredHandlerException.forCommand(command)
}