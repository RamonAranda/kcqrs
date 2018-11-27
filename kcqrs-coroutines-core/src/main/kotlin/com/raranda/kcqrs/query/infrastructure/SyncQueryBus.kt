package com.raranda.kcqrs.query.infrastructure

import com.raranda.kcqrs.shared.DefaultQueryCoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class SyncQueryBus(customCoroutineContext: CoroutineContext = DefaultQueryCoroutineDispatcher) : AbstractQueryBus(customCoroutineContext) {
    override suspend fun <R> executeWith(coroutineContext: CoroutineContext, block: suspend () -> R): R {
        return withContext(coroutineContext) { block() }
    }
}