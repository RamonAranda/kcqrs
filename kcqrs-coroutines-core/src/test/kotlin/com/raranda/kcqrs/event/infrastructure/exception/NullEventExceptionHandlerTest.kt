package com.raranda.kcqrs.event.infrastructure.exception

import com.raranda.kcqrs.event.domain.DomainEvent
import com.raranda.kcqrs.event.domain.EventHandler
import io.kotlintest.specs.ShouldSpec
import kotlinx.coroutines.runBlocking

class NullEventExceptionHandlerTest : ShouldSpec(
        {
            class TestEvent : DomainEvent
            class TestHandler : EventHandler<TestEvent> {
                override suspend fun on(event: TestEvent) {}
            }
            val exceptionHandler = NullEventExceptionHandler()
            should("do nothing") {
                runBlocking {
                    val exception = RuntimeException("Potato")
                    val event = TestEvent()
                    val handler = TestHandler()
                    exceptionHandler.on(event, handler, exception)
                }
            }
        })