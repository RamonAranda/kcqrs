package com.raranda.kcqrs.event.domain

interface EventExceptionHandler {
    fun on(event: DomainEvent, handler: EventHandler<*>, exception: Exception)
}