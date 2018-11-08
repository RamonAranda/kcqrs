package com.raranda.kcqrs.command

import com.raranda.kcqrs.command.domain.Command
import com.raranda.kcqrs.command.domain.CommandHandler
import com.raranda.kcqrs.command.infrastructure.SuspendableCommandBus
import com.raranda.kcqrs.shared.NoRegisteredHandlerException
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.ShouldSpec
import kotlin.test.assertFailsWith

class SuspendableCommandBusTest: ShouldSpec(
        {
            data class TestCommand(val name: String): Command
            class TestThreadAwareCommandHandler: CommandHandler<TestCommand, String> {
                var threadId: Long? = null
                override fun on(command: TestCommand): String {
                    threadId = Thread.currentThread().id
                    return command.name
                }
            }
            val expected = "Potato"
            val command = TestCommand(expected)
            val handler = TestThreadAwareCommandHandler()
            val commandBus = SuspendableCommandBus().apply { register(handler) }

            should("handle command successfully") {
                commandBus.handle<String>(command) shouldBe expected
                handler.threadId shouldNotBe Thread.currentThread().id
            }

            should("raise exception if no handler is found") {
                class FailingTestCommand: Command
                val failingCommand = FailingTestCommand()
                val exception = assertFailsWith<NoRegisteredHandlerException> {
                    commandBus.handle<String>(failingCommand)
                }
                exception.message shouldBe "No handler was registered for <${failingCommand.javaClass.kotlin.simpleName}>."
            }
        })