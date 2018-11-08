package com.raranda.kcqrs.command.infrastructure

import com.raranda.kcqrs.command.domain.Command
import com.raranda.kcqrs.command.domain.CommandBus
import com.raranda.kcqrs.command.domain.CommandHandler
import com.raranda.kcqrs.shared.DefaultCommandCoroutineDispatcher
import com.raranda.kcqrs.shared.NoRegisteredHandlerException
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass

class SuspendableCommandBus(private val customCoroutineContext: CoroutineContext = DefaultCommandCoroutineDispatcher) : CommandBus {
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