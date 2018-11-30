package com.raranda.kcqrs.event.error_handler

import com.raranda.kcqrs.event.domain.DomainEvent
import com.raranda.kcqrs.event.domain.EventHandler
import com.raranda.kcqrs.event.infrastructure.exception_handlers.NullEventExceptionHandler
import io.kotlintest.specs.ShouldSpec

class NullEventExceptionHandlerTest: ShouldSpec(
        {
            val errorHandler = NullEventExceptionHandler()
            class TestEvent: DomainEvent
            class TestEventHandler: EventHandler<TestEvent> {
                override fun on(event: TestEvent) {}
            }
            val event = TestEvent()
            val handler = TestEventHandler()

            should("do nothing") {
                errorHandler.on(event, handler, RuntimeException())
            }
        })