package com.raranda.kcqrs.event.infrastructure.exception_handlers

import com.raranda.kcqrs.event.domain.DomainEvent
import com.raranda.kcqrs.event.domain.EventExceptionHandler
import com.raranda.kcqrs.event.domain.EventHandler

class NullEventExceptionHandler: EventExceptionHandler {
    override fun on(event: DomainEvent, handler: EventHandler<*>, exception: Exception) {
        //NOOP
    }
}