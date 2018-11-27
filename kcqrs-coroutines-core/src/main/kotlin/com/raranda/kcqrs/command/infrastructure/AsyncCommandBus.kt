package com.raranda.kcqrs.command.infrastructure

import com.raranda.kcqrs.shared.DefaultCommandCoroutineDispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class AsyncCommandBus(customCoroutineContext: CoroutineContext = DefaultCommandCoroutineDispatcher) : AbstractCommandBus(customCoroutineContext) {
    override suspend fun <R> executeWith(coroutineContext: CoroutineContext, block: suspend () -> R): R {
        GlobalScope.launch(coroutineContext) { block() }
        @Suppress("UNCHECKED_CAST")
        return Unit as R
    }
}