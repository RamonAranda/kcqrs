package com.raranda.kcqrs.command.infrastructure

import com.raranda.kcqrs.command.domain.Command
import com.raranda.kcqrs.command.domain.CommandHandler
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.ShouldSpec
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class AsyncCommandBusTest : ShouldSpec(
        {
            class TestCommand : Command
            class TestCommandHandler : CommandHandler<TestCommand, Unit> {
                var threadId: Long = 0L
                override suspend fun on(command: TestCommand) {
                    delay(100)
                    threadId = Thread.currentThread().id
                }
            }
            should("handle command in a different thread") {
                runBlocking {
                    val commandHandler = TestCommandHandler()
                    val commandBus = AsyncCommandBus().apply { register(commandHandler) }
                    commandBus.handle<Long>(TestCommand()) shouldBe Unit
                    commandHandler.threadId shouldBe 0L
                    Thread.sleep(200)
                    commandHandler.threadId shouldNotBe 0L
                    commandHandler.threadId shouldNotBe Thread.currentThread().id
                }
            }
        })