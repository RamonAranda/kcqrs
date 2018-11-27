package com.raranda.kcqrs.query.domain

interface QueryBus {
    suspend fun <Q: Query> register(handler: QueryHandler<Q, *>)
    suspend fun <R> handle(query: Query): R
}