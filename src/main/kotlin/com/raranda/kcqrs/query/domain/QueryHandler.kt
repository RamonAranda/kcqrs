package com.raranda.kcqrs.query.domain

interface QueryHandler<in Q: Query, R> {
    fun on(query: Q): R
}