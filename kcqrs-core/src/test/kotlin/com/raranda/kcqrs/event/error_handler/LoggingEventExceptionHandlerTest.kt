package com.raranda.kcqrs.event.error_handler

import com.raranda.kcqrs.event.domain.DomainEvent
import com.raranda.kcqrs.event.domain.EventHandler
import com.raranda.kcqrs.event.infrastructure.exception_handlers.LoggingEventExceptionHandler
import io.kotlintest.specs.ShouldSpec
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import org.slf4j.Logger

class LoggingEventExceptionHandlerTest: ShouldSpec(
        {
            val logger: Logger = mockk()
            val errorHandler = LoggingEventExceptionHandler(logger)
            class TestEvent: DomainEvent
            class TestEventHandler: EventHandler<TestEvent> {
                override fun on(event: TestEvent) {}
            }
            val event = TestEvent()
            val handler = TestEventHandler()

            should("call logger error with custom message") {
                val message = "Failed handling event <${event::class.simpleName}> at handler <${handler::class.simpleName}> due:"
                val exceptionMessage = "Something went wrong"
                val exception = RuntimeException(exceptionMessage)
                logger.shouldLogError(message, exception)
                errorHandler.on(event, handler, exception)
            }
        })

private fun Logger.shouldLogError(message: String, exception: Exception) {
    every { this@shouldLogError.error(message, exception) } just runs
}