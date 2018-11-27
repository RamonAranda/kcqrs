package com.raranda.kcqrs.command.infrastructure

import com.raranda.kcqrs.command.domain.Command
import com.raranda.kcqrs.command.domain.CommandBus
import com.raranda.kcqrs.command.domain.CommandHandler
import com.raranda.kcqrs.shared.NoRegisteredHandlerException
import com.raranda.kcqrs.shared.firstSuperClassGeneric
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass

abstract class AbstractCommandBus(private val customCoroutineContext: CoroutineContext) : CommandBus {
    private val handlers: MutableMap<KClass<*>, CommandHandler<*, *>> = mutableMapOf()

    override suspend fun <C : Command> register(handler: CommandHandler<C, *>) {
        handlers[handler.firstSuperClassGeneric()!!] = handler
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun <R> handle(command: Command): R {
        val handler = handlers[command::class] as? CommandHandler<Command, R>
                      ?: throw NoRegisteredHandlerException.forCommand(command)
        return executeWith(customCoroutineContext) { handler.on(command) }
    }

    protected abstract suspend fun <R> executeWith(coroutineContext: CoroutineContext, block: suspend () -> R): R
}