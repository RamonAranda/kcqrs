package com.raranda.kcqrs.query.domain

interface QueryHandler<in Q : Query, R> {
    suspend fun on(query: Q): R
}