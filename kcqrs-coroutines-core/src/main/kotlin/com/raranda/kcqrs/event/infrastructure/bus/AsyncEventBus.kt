package com.raranda.kcqrs.event.infrastructure.bus

import com.raranda.kcqrs.event.domain.EventExceptionHandler
import com.raranda.kcqrs.shared.DefaultEventCoroutineDispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class AsyncEventBus(exceptionHandler: EventExceptionHandler,
                    coroutineContext: CoroutineContext = DefaultEventCoroutineDispatcher) :
        AbstractEventBus(exceptionHandler, coroutineContext) {
    override suspend fun executeWith(coroutineContext: CoroutineContext, block: suspend () -> Unit) {
        GlobalScope.launch(coroutineContext) { block() }
    }
}