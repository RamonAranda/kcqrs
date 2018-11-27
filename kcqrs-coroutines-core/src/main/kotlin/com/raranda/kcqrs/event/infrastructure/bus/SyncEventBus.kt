package com.raranda.kcqrs.event.infrastructure.bus

import com.raranda.kcqrs.event.domain.EventExceptionHandler
import com.raranda.kcqrs.event.infrastructure.exception.LoggingEventExceptionHandler
import com.raranda.kcqrs.shared.DefaultEventCoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class SyncEventBus(exceptionHandler: EventExceptionHandler = LoggingEventExceptionHandler(),
                   coroutineContext: CoroutineContext = DefaultEventCoroutineDispatcher) :
        AbstractEventBus(exceptionHandler, coroutineContext) {
    override suspend fun executeWith(coroutineContext: CoroutineContext, block: suspend () -> Unit) {
        withContext(coroutineContext) { block() }
    }
}