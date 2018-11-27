package com.raranda.kcqrs.event.domain

interface EventExceptionHandler {
    suspend fun on(event: DomainEvent, handler: EventHandler<*>, exception: Exception)
}