package com.raranda.kcqrs.command.infrastructure

import com.raranda.kcqrs.command.domain.Command
import com.raranda.kcqrs.command.domain.CommandHandler
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.ShouldSpec
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.EmptyCoroutineContext

class SyncCommandBusTest : ShouldSpec(
        {
            class TestCommand : Command
            class TestCommandHandler : CommandHandler<TestCommand, Long> {
                override suspend fun on(command: TestCommand) = Thread.currentThread().id
            }
            should("handle command in a different thread") {
                runBlocking {
                    val commandBus = SyncCommandBus().apply { register(TestCommandHandler()) }
                    commandBus.handle<Long>(TestCommand()) shouldNotBe Thread.currentThread().id
                }
            }

            should("handle command in same thread because is empty coroutine context") {
                runBlocking {
                    val commandBus = SyncCommandBus(EmptyCoroutineContext)
                            .apply { register(TestCommandHandler()) }
                    commandBus.handle<Long>(TestCommand()) shouldBe Thread.currentThread().id
                }
            }
        })