package com.raranda.kcqrs.event.domain

interface EventBus {
    fun <E: DomainEvent> register(handler: EventHandler<E>)
    fun send(event: DomainEvent)
}