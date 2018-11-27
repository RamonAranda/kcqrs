package com.raranda.kcqrs.event.infrastructure.bus

import com.raranda.kcqrs.event.domain.DomainEvent
import com.raranda.kcqrs.event.domain.EventExceptionHandler
import com.raranda.kcqrs.event.domain.EventHandler
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.ShouldSpec
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class AsyncEventBusTest : ShouldSpec(
        {
            val exceptionHandler = mockk<EventExceptionHandler>()

            class TestEvent : DomainEvent
            class TestHandler : EventHandler<TestEvent> {
                var threadId: Long = 0L
                override suspend fun on(event: TestEvent) {
                    threadId = Thread.currentThread().id
                }
            }
            should("handle event asynchronously") {
                runBlocking {
                    val handler = TestHandler()
                    val bus = AsyncEventBus(exceptionHandler).apply { register(handler) }
                    bus.publish(TestEvent())
                    delay(100)
                    handler.threadId shouldNotBe Thread.currentThread().id
                }
            }

            should("call exception handler when an exception is thrown at event handler") {
                runBlocking {
                    val exception = RuntimeException("Potato")

                    class TestEventHandler : EventHandler<TestEvent> {
                        override suspend fun on(event: TestEvent) {
                            throw exception
                        }
                    }

                    val event = TestEvent()
                    val handler = TestEventHandler()

                    exceptionHandler.shouldHandle(event, handler, exception)

                    AsyncEventBus(exceptionHandler).apply { register(handler) }.publish(event)

                    delay(100)
                }
            }
        })

private suspend fun EventExceptionHandler.shouldHandle(event: DomainEvent,
                                                       handler: EventHandler<*>,
                                                       exception: Exception) {
    coEvery { on(event, handler, exception) } just runs
}