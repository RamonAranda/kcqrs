package com.raranda.kcqrs.event.infrastructure

import com.raranda.kcqrs.event.domain.DomainEvent
import com.raranda.kcqrs.event.domain.EventBus
import com.raranda.kcqrs.event.domain.EventHandler

class AsyncEventBus: EventBus {
    override fun <E : DomainEvent> register(handler: EventHandler<E>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun publish(event: DomainEvent) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}