package com.raranda.kcqrs.command.domain

interface CommandBus {
    fun <C : Command> register(handler: CommandHandler<C, *>)
    fun <R> handle(command: Command): R
}