package com.raranda.kcqrs.event.infrastructure.bus

import com.raranda.kcqrs.event.domain.DomainEvent
import com.raranda.kcqrs.event.domain.EventBus
import com.raranda.kcqrs.event.domain.EventExceptionHandler
import com.raranda.kcqrs.event.domain.EventHandler
import com.raranda.kcqrs.shared.firstSuperClassGeneric
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass

abstract class AbstractEventBus(override val exceptionHandler: EventExceptionHandler,
                                private val coroutineContext: CoroutineContext) : EventBus {
    private val handlers: MutableMap<KClass<*>, MutableList<EventHandler<*>>> = mutableMapOf()

    override suspend fun <E : DomainEvent> register(handler: EventHandler<E>) {
        val eventClass = handler.firstSuperClassGeneric()!!
        handlers.computeIfAbsent(eventClass) { mutableListOf() }
        handlers[eventClass]!!.add(handler)
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun publish(event: DomainEvent) {
        handlers[event::class]?.run {
            asSequence()
                    .map { it as EventHandler<DomainEvent> }
                    .forEach {
                        executeWith(coroutineContext) {
                            try {
                                it.on(event)
                            } catch (exception: Exception) {
                                exceptionHandler.on(event, it, exception)
                            }
                        }
                    }
        }
    }

    protected abstract suspend fun executeWith(coroutineContext: CoroutineContext, block: suspend () -> Unit)
}