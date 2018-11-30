package com.raranda.kcqrs.event.bus

import com.raranda.kcqrs.event.domain.DomainEvent
import com.raranda.kcqrs.event.domain.EventExceptionHandler
import com.raranda.kcqrs.event.domain.EventHandler
import com.raranda.kcqrs.event.infrastructure.bus.SyncEventBus
import io.kotlintest.shouldBe
import io.kotlintest.specs.ShouldSpec
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs

class SyncEventBusTest : ShouldSpec(
        {
            val exceptionHandler: EventExceptionHandler = mockk()
            data class TestEvent(val name: String) : DomainEvent
            class TestEventHandler : EventHandler<TestEvent> {
                var name: String? = null
                override fun on(event: TestEvent) {
                    name = event.name
                }
            }
            val name = "Potato"
            val event = TestEvent(name)

            should("handle event successfully") {
                val handler = TestEventHandler()
                val eventBus = SyncEventBus(exceptionHandler).apply { register(handler) }
                eventBus.publish(event)
                handler.name shouldBe name
            }

            should("handle event in multiple event handlers") {
                val handler = TestEventHandler()
                val anotherHandler = TestEventHandler()
                val eventBus = SyncEventBus(exceptionHandler).apply {
                    register(handler)
                    register(anotherHandler)
                }
                eventBus.publish(event)
                handler.name shouldBe name
                anotherHandler.name shouldBe name
            }

            should("call exception handler if exception was thrown when handling event") {
                val exceptionMessage = "Potato"
                val exception = RuntimeException(exceptionMessage)
                class FailingTestEventHandler : EventHandler<TestEvent> {
                    override fun on(event: TestEvent) {
                        throw exception
                    }
                }
                val failingHandler = FailingTestEventHandler()
                val eventBus = SyncEventBus(exceptionHandler).apply { register(failingHandler) }
                exceptionHandler.shouldHandleException(event, failingHandler, exception)
                eventBus.publish(event)
            }
        })

private fun EventExceptionHandler.shouldHandleException(event: DomainEvent, handler: EventHandler<*>, exception: Exception) {
    every { this@shouldHandleException.on(event, handler, exception) } just runs
}