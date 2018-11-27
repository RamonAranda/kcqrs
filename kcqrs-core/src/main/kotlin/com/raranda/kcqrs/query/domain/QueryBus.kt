package com.raranda.kcqrs.query.domain

interface QueryBus {
    fun <Q : Query> register(handler: QueryHandler<Q, *>)
    fun <R> handle(query: Query): R
}