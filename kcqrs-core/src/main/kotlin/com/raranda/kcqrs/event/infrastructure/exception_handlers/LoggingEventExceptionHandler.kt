package com.raranda.kcqrs.event.infrastructure.exception_handlers

import com.raranda.kcqrs.event.domain.DomainEvent
import com.raranda.kcqrs.event.domain.EventExceptionHandler
import com.raranda.kcqrs.event.domain.EventHandler
import com.raranda.kcqrs.shared.logger
import org.slf4j.Logger

class LoggingEventExceptionHandler(private val logger: Logger = LoggingEventExceptionHandler::class.logger()): EventExceptionHandler {

    override fun on(event: DomainEvent, handler: EventHandler<*>, exception: Exception) {
        logger.error("Failed handling event <${event::class.simpleName}> at handler <${handler::class.simpleName}> due:", exception)
    }
}