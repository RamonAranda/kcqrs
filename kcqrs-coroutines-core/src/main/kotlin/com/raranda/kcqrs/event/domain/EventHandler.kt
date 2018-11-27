package com.raranda.kcqrs.event.domain

interface EventHandler<E: DomainEvent>{
    suspend fun on(event: E)
}