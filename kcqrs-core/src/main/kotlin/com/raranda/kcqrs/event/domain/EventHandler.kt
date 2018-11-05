package com.raranda.kcqrs.event.domain

interface EventHandler<E: DomainEvent>{
    fun on(event: E)
}