package com.raranda.kcqrs.event.domain

interface EventBus {
    val exceptionHandler: EventExceptionHandler
    suspend fun <E: DomainEvent> register(handler: EventHandler<E>)
    suspend fun publish(event: DomainEvent)
}