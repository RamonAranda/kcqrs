package com.raranda.kcqrs.query

import com.raranda.kcqrs.query.domain.Query
import com.raranda.kcqrs.query.domain.QueryHandler
import com.raranda.kcqrs.query.infrastructure.SuspendableQueryBus
import com.raranda.kcqrs.shared.NoRegisteredHandlerException
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.ShouldSpec
import kotlin.test.assertFailsWith

class SuspendableQueryBusTest: ShouldSpec(
        {
            data class TestQuery(val name: String): Query
            class TestThreadAwareQueryHandler: QueryHandler<TestQuery, String> {
                var threadId: Long? = null
                override fun on(query: TestQuery): String {
                    threadId = Thread.currentThread().id
                    return query.name
                }
            }
            val expected = "Potato"
            val query = TestQuery(expected)
            val handler = TestThreadAwareQueryHandler()
            val queryBus = SuspendableQueryBus().apply { register(handler) }

            should("it should handle query successfully") {
                queryBus.handle<String>(query) shouldBe expected
                handler.threadId shouldNotBe Thread.currentThread().id
            }

            should("it should raise exception if no handler is found") {
                class FailingTestQuery: Query
                val failingQuery = FailingTestQuery()
                val exception = assertFailsWith<NoRegisteredHandlerException> {
                    queryBus.handle<String>(failingQuery)
                }
                exception.message shouldBe "No handler was registered for <${failingQuery.javaClass.kotlin.simpleName}>."
            }
        })