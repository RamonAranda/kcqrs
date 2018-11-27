package com.raranda.kcqrs.event.infrastructure.bus

import com.raranda.kcqrs.event.domain.DomainEvent
import com.raranda.kcqrs.event.domain.EventBus
import com.raranda.kcqrs.event.domain.EventExceptionHandler
import com.raranda.kcqrs.event.domain.EventHandler
import com.raranda.kcqrs.event.infrastructure.exception_handlers.LoggingEventExceptionHandler
import com.raranda.kcqrs.shared.firstSuperClassGeneric
import kotlin.reflect.KClass

class SyncEventBus(override val exceptionHandler: EventExceptionHandler = LoggingEventExceptionHandler()) : EventBus {
    private val handlers: MutableMap<KClass<*>, MutableList<EventHandler<*>>> = mutableMapOf()

    override fun <E : DomainEvent> register(handler: EventHandler<E>) {
        val eventClass = handler.firstSuperClassGeneric()!!
        handlers.computeIfAbsent(eventClass) { mutableListOf() }
        handlers[eventClass]!!.add(handler)
    }

    override fun publish(event: DomainEvent) {
        handlers[event::class]?.run {
            @Suppress("UNCHECKED_CAST")
            asSequence().map { it as EventHandler<DomainEvent> }.forEach {
                try {
                    it.on(event)
                } catch (exception: Exception) {
                    exceptionHandler.on(event, it, exception)
                }
            }
        }
    }
}