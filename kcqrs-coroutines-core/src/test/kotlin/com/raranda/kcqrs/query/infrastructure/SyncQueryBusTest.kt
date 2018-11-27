package com.raranda.kcqrs.query.infrastructure

import com.raranda.kcqrs.query.domain.Query
import com.raranda.kcqrs.query.domain.QueryHandler
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.ShouldSpec
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.EmptyCoroutineContext

class SyncQueryBusTest : ShouldSpec(
        {
            class TestQuery : Query
            class TestQueryHandler : QueryHandler<TestQuery, Long> {
                override suspend fun on(query: TestQuery) = Thread.currentThread().id
            }
            should("handle query in a different thread") {
                runBlocking {
                    val queryBus = SyncQueryBus().apply { register(TestQueryHandler()) }
                    queryBus.handle<Long>(TestQuery()) shouldNotBe Thread.currentThread().id
                }
            }

            should("handle query in same thread because is empty coroutine context") {
                runBlocking {
                    val queryBus = SyncQueryBus(EmptyCoroutineContext)
                            .apply { register(TestQueryHandler()) }
                    queryBus.handle<Long>(TestQuery()) shouldBe Thread.currentThread().id
                }
            }
        })