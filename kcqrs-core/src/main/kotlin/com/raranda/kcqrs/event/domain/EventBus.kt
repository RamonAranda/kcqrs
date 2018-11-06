package com.raranda.kcqrs.event.domain

interface EventBus {
    val exceptionHandler: EventExceptionHandler
    fun <E: DomainEvent> register(handler: EventHandler<E>)
    fun publish(event: DomainEvent)
}