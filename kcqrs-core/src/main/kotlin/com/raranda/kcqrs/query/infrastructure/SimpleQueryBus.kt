package com.raranda.kcqrs.query.infrastructure

import com.raranda.kcqrs.query.domain.Query
import com.raranda.kcqrs.query.domain.QueryBus
import com.raranda.kcqrs.query.domain.QueryHandler
import com.raranda.kcqrs.shared.NoRegisteredHandlerException
import kotlin.reflect.KClass

    class SimpleQueryBus : QueryBus {
        private val handlers: MutableMap<KClass<*>, QueryHandler<*, *>> = mutableMapOf()

        override fun <Q : Query> register(handler: QueryHandler<Q, *>) {
            handlers[handler.queryClass()] = handler
        }

        @Suppress("UNCHECKED_CAST")
        override fun <R> handle(query: Query): R =
                (handlers[query::class] as? QueryHandler<Query, R>)?.on(query)
                ?: throw NoRegisteredHandlerException.forQuery(query)
    }