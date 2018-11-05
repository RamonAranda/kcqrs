package com.raranda.kcqrs.event.infrastructure

import com.raranda.kcqrs.event.domain.DomainEvent
import com.raranda.kcqrs.event.domain.EventBus
import com.raranda.kcqrs.event.domain.EventHandler
import kotlin.reflect.KClass

class SyncEventBus: EventBus {
    private val handlers: MutableMap<KClass<*>, MutableList<EventHandler<*>>> = mutableMapOf()

    override fun <E : DomainEvent> register(handler: EventHandler<E>) {
        val eventClass = handler.eventClass()
        handlers.computeIfAbsent(eventClass) { mutableListOf() }
        handlers[eventClass]!!.add(handler)
    }

    @Suppress("UNCHECKED_CAST")
    override fun publish(event: DomainEvent) {
        handlers[event::class]
                ?.asSequence()
                ?.map { it as EventHandler<DomainEvent> }
                ?.forEach { it.on(event) } //TODO: Handle exceptions
    }
}