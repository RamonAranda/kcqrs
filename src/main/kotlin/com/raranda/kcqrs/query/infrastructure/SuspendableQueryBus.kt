package com.raranda.kcqrs.query.infrastructure

import com.raranda.kcqrs.query.domain.Query
import com.raranda.kcqrs.query.domain.QueryBus
import com.raranda.kcqrs.query.domain.QueryHandler
import com.raranda.kcqrs.shared.NoRegisteredHandlerException
import kotlinx.coroutines.experimental.DefaultDispatcher
import kotlinx.coroutines.experimental.runBlocking
import kotlinx.coroutines.experimental.withContext
import kotlin.coroutines.experimental.CoroutineContext
import kotlin.reflect.KClass

class SuspendableQueryBus(private val customCoroutineContext: CoroutineContext = DefaultDispatcher) : QueryBus {
    private val handlers: MutableMap<KClass<*>, QueryHandler<*, *>> = mutableMapOf()

    override fun <Q : Query> register(handler: QueryHandler<Q, *>) {
        handlers[handler.queryClass()] = handler
    }

    override fun <R> handle(query: Query): R {
        @Suppress("UNCHECKED_CAST")
        val handler = handlers[query::class] as? QueryHandler<Query, R>
                      ?: throw NoRegisteredHandlerException.forQuery(query)
        return runBlocking(customCoroutineContext) {
            withContext(customCoroutineContext) { handler.on(query) }
        }
    }
}