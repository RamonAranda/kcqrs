package com.raranda.kcqrs.command.infrastructure

import com.raranda.kcqrs.command.domain.Command
import com.raranda.kcqrs.command.domain.CommandBus
import com.raranda.kcqrs.command.domain.CommandHandler
import com.raranda.kcqrs.shared.NoRegisteredHandlerException
import kotlinx.coroutines.experimental.DefaultDispatcher
import kotlinx.coroutines.experimental.runBlocking
import kotlinx.coroutines.experimental.withContext
import kotlin.coroutines.experimental.CoroutineContext
import kotlin.reflect.KClass

class SuspendableCommandBus(private val customCoroutineContext: CoroutineContext = DefaultDispatcher) : CommandBus {
    private val handlers: MutableMap<KClass<*>, CommandHandler<*, *>> = mutableMapOf()

    override fun <C : Command> register(handler: CommandHandler<C, *>) {
        handlers[handler.commandClass()] = handler
    }

    @Suppress("UNCHECKED_CAST")
    override fun <R> handle(command: Command): R {
        @Suppress("UNCHECKED_CAST")
        val handler = handlers[command::class] as? CommandHandler<Command, R>
                      ?: throw NoRegisteredHandlerException.forCommand(command)
        return runBlocking(customCoroutineContext) {
            withContext(customCoroutineContext) { handler.on(command) }
        }
    }
}