package com.raranda.kcqrs.event

import com.raranda.kcqrs.event.domain.DomainEvent
import com.raranda.kcqrs.event.domain.EventHandler
import com.raranda.kcqrs.event.infrastructure.SyncEventBus
import io.kotlintest.shouldBe
import io.kotlintest.specs.ShouldSpec

class SyncEventBusTest : ShouldSpec(
        {
            data class TestEvent(val name: String): DomainEvent
            class TestEventHandler: EventHandler<TestEvent> {
                var name: String? = null
                override fun on(event: TestEvent) {
                    name = event.name
                }
            }
            val name = "Potato"
            val event = TestEvent(name)
            val handler = TestEventHandler()
            val eventBus = SyncEventBus().apply { register(handler) }

            should("handle event successfully") {
                eventBus.publish(event)
                handler.name shouldBe name
            }

            should("handle event in multiple event handlers") {
                val anotherHandler = TestEventHandler()
                eventBus.register(anotherHandler)
                eventBus.publish(event)
                handler.name shouldBe name
                anotherHandler.name shouldBe name
            }
        })