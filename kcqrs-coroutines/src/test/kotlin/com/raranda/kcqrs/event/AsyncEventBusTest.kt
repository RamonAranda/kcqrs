package com.raranda.kcqrs.event

import com.raranda.kcqrs.event.domain.DomainEvent
import com.raranda.kcqrs.event.domain.EventExceptionHandler
import com.raranda.kcqrs.event.domain.EventHandler
import com.raranda.kcqrs.event.infrastructure.bus.AsyncEventBus
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.ShouldSpec
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify

class AsyncEventBusTest : ShouldSpec(
        {
            val exceptionHandler: EventExceptionHandler = mockk()

            data class TestEvent(val name: String) : DomainEvent
            class TestEventHandler : EventHandler<TestEvent> {
                var name: String? = null
                var threadId: Long? = null
                override fun on(event: TestEvent) {
                    name = event.name
                    threadId = Thread.currentThread().id
                }
            }

            val name = "Potato"
            val event = TestEvent(name)

            should("handle event asynchronously") {
                val handler = TestEventHandler()
                val eventBus = AsyncEventBus(exceptionHandler).apply { register(handler) }
                eventBus.publish(event)
                Thread.sleep(500)
                handler.name shouldBe name
                handler.threadId shouldNotBe Thread.currentThread().id
            }

            should("handle event in multiple handlers asynchronously") {
                val handler = TestEventHandler()
                val anotherHandler = TestEventHandler()
                val eventBus = AsyncEventBus(exceptionHandler).apply {
                    register(handler)
                    register(anotherHandler)
                }
                eventBus.publish(event)
                Thread.sleep(500)
                handler.name shouldBe name
                anotherHandler.name shouldBe name
                val currentThread = Thread.currentThread().id
                handler.threadId shouldNotBe currentThread
                anotherHandler.threadId shouldNotBe currentThread
                handler.threadId shouldNotBe anotherHandler.threadId
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
                val eventBus = AsyncEventBus(exceptionHandler).apply { register(failingHandler) }
                exceptionHandler.shouldHandleException(event, failingHandler, exception)
                eventBus.publish(event)
                Thread.sleep(500)
                exceptionHandler.verifyCalledOnce(event, failingHandler, exception)
            }
        })

private fun EventExceptionHandler.shouldHandleException(event: DomainEvent, handler: EventHandler<*>, exception: Exception) {
    every { this@shouldHandleException.on(event, handler, exception) } just runs
}

private fun EventExceptionHandler.verifyCalledOnce(event: DomainEvent, handler: EventHandler<*>, exception: Exception) {
    verify(exactly = 1) { this@verifyCalledOnce.on(event, handler, exception) }
}