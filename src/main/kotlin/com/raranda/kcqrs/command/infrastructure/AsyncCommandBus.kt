package com.raranda.kcqrs.command.infrastructure

import com.raranda.kcqrs.command.domain.Command
import com.raranda.kcqrs.command.domain.CommandBus
import com.raranda.kcqrs.command.domain.CommandHandler

class AsyncCommandBus: CommandBus {
    override fun <C : Command> register(handler: CommandHandler<C, *>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <R> handle(command: Command): R {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}