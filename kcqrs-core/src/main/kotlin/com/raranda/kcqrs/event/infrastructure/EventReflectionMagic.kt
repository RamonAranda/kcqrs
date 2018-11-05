package com.raranda.kcqrs.event.infrastructure

import com.raranda.kcqrs.event.domain.DomainEvent
import com.raranda.kcqrs.event.domain.EventHandler
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.jvmErasure

internal fun <E : DomainEvent> EventHandler<E>.eventClass() =
        javaClass.kotlin
                .supertypes.first { it.jvmErasure.isSubclassOf(EventHandler::class) }
                .arguments.first()
                .type!!.jvmErasure