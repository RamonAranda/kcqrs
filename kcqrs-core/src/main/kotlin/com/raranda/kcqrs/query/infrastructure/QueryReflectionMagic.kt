package com.raranda.kcqrs.query.infrastructure

import com.raranda.kcqrs.query.domain.Query
import com.raranda.kcqrs.query.domain.QueryHandler
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.jvmErasure

fun <Q : Query> QueryHandler<Q, *>.queryClass() =
        javaClass.kotlin
                .supertypes.first { it.jvmErasure.isSubclassOf(QueryHandler::class) }
                .arguments.first()
                .type!!.jvmErasure