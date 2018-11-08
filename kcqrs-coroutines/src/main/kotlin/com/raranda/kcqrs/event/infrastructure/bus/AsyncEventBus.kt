package com.raranda.kcqrs.event.infrastructure.bus

import com.raranda.kcqrs.event.domain.DomainEvent
import com.raranda.kcqrs.event.domain.EventBus
import com.raranda.kcqrs.event.domain.EventExceptionHandler
import com.raranda.kcqrs.event.domain.EventHandler
import com.raranda.kcqrs.event.infrastructure.exception_handlers.LoggingEventExceptionHandler
import com.raranda.kcqrs.shared.DefaultEventCoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass

class AsyncEventBus(override val exceptionHandler: EventExceptionHandler = LoggingEventExceptionHandler(),
                    private val coroutineContext: CoroutineContext = DefaultEventCoroutineDispatcher) : EventBus {
    private val handlers: MutableMap<KClass<*>, MutableList<EventHandler<*>>> = mutableMapOf()

    override fun <E : DomainEvent> register(handler: EventHandler<E>) {
        val eventClass = handler.eventClass()
        handlers.computeIfAbsent(eventClass) { mutableListOf() }
        handlers[eventClass]!!.add(handler)
    }

    override fun publish(event: DomainEvent) {
        runBlocking(coroutineContext) {
            handlers[event::class]?.run {
                @Suppress("UNCHECKED_CAST")
                asSequence().map { it as EventHandler<DomainEvent> }.forEach {
                    launch(coroutineContext) {
                        try {
                            it.on(event)
                        } catch (exception: Exception) {
                            exceptionHandler.on(event, it, exception)
                        }
                    }
                }
            }
        }
    }
}