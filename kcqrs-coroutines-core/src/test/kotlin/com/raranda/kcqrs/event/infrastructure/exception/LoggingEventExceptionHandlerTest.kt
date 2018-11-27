package com.raranda.kcqrs.event.infrastructure.exception

import com.raranda.kcqrs.event.domain.DomainEvent
import com.raranda.kcqrs.event.domain.EventHandler
import io.kotlintest.specs.ShouldSpec
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger

class LoggingEventExceptionHandlerTest : ShouldSpec(
        {
            class TestEvent : DomainEvent
            class TestHandler : EventHandler<TestEvent> {
                override suspend fun on(event: TestEvent) {}
            }
            should("log error") {
                runBlocking {
                    val logger = mockk<Logger>()
                    val exceptionHandler = LoggingEventExceptionHandler(logger)
                    val exception = RuntimeException("Potato")
                    val event = TestEvent()
                    val handler = TestHandler()
                    val message = "Failed handling event <${event::class.simpleName}> at handler <${handler::class.simpleName}> due:"
                    logger.shouldLogError(message, exception)
                    exceptionHandler.on(event, handler, exception)
                }
            }
        })

private fun Logger.shouldLogError(message: String, exception: Exception) {
    every { error(message, exception) } just runs
}