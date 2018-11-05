package com.raranda.kcqrs.query

import com.raranda.kcqrs.query.domain.Query
import com.raranda.kcqrs.query.domain.QueryHandler
import com.raranda.kcqrs.query.infrastructure.SimpleQueryBus
import com.raranda.kcqrs.shared.NoRegisteredHandlerException
import io.kotlintest.shouldBe
import io.kotlintest.specs.ShouldSpec
import kotlin.test.assertFailsWith

class SimpleQueryBusTest: ShouldSpec(
        {
            data class TestQuery(val name: String): Query
            class TestQueryHandler: QueryHandler<TestQuery, String> {
                override fun on(query: TestQuery) = query.name
            }
            val expected = "Potato"
            val query = TestQuery(expected)
            val handler = TestQueryHandler()
            val queryBus = SimpleQueryBus().apply { register(handler) }

            should("it should handle query successfully") {
                queryBus.handle<String>(query) shouldBe expected
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