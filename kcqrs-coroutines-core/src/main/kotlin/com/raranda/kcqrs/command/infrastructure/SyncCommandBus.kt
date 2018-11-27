package com.raranda.kcqrs.command.infrastructure

import com.raranda.kcqrs.shared.DefaultCommandCoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class SyncCommandBus(customCoroutineContext: CoroutineContext = DefaultCommandCoroutineDispatcher) : AbstractCommandBus(customCoroutineContext) {
    override suspend fun <R> executeWith(coroutineContext: CoroutineContext, block: suspend () -> R): R {
        return withContext(coroutineContext) { block() }
    }
}