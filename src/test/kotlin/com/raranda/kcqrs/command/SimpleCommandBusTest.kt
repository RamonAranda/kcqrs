package com.raranda.kcqrs.command

import com.raranda.kcqrs.command.domain.Command
import com.raranda.kcqrs.command.domain.CommandHandler
import com.raranda.kcqrs.command.infrastructure.SimpleCommandBus
import com.raranda.kcqrs.shared.NoRegisteredHandlerException
import io.kotlintest.shouldBe
import io.kotlintest.specs.ShouldSpec
import kotlin.test.assertFailsWith

class SimpleCommandBusTest: ShouldSpec(
        {
            data class TestCommand(val name: String): Command
            class TestCommandHandler: CommandHandler<TestCommand, String> {
                override fun on(command: TestCommand) = command.name
            }
            val expected = "Potato"
            val query = TestCommand(expected)
            val handler = TestCommandHandler()
            val commandHandler = SimpleCommandBus().apply { register(handler) }

            should("it should handle command successfully") {
                commandHandler.handle<String>(query) shouldBe expected
            }

            should("it should raise exception if no handler is found") {
                class FailingTestCommand: Command
                val failingCommand = FailingTestCommand()
                val exception = assertFailsWith<NoRegisteredHandlerException> {
                    commandHandler.handle<Unit>(failingCommand)
                }
                exception.message shouldBe "No handler was registered for <${failingCommand.javaClass.kotlin.simpleName}>."
            }
        })