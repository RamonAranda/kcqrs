package com.raranda.kcqrs.query.infrastructure

import com.raranda.kcqrs.query.domain.Query
import com.raranda.kcqrs.query.domain.QueryBus
import com.raranda.kcqrs.query.domain.QueryHandler
import com.raranda.kcqrs.shared.NoRegisteredHandlerException
import com.raranda.kcqrs.shared.firstSuperClassGeneric
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass

abstract class AbstractQueryBus(private val customCoroutineContext: CoroutineContext) : QueryBus {
    private val handlers: MutableMap<KClass<*>, QueryHandler<*, *>> = mutableMapOf()

    override suspend fun <Q : Query> register(handler: QueryHandler<Q, *>) {
        handlers[handler.firstSuperClassGeneric()!!] = handler
    }

    override suspend fun <R> handle(query: Query): R {
        @Suppress("UNCHECKED_CAST")
        val handler = handlers[query::class] as? QueryHandler<Query, R>
                      ?: throw NoRegisteredHandlerException.forQuery(query)
        return executeWith(customCoroutineContext) { handler.on(query) }
    }

    protected abstract suspend fun <R> executeWith(coroutineContext: CoroutineContext, block: suspend () -> R): R
}