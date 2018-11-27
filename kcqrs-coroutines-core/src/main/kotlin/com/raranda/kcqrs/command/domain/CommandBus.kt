package com.raranda.kcqrs.command.domain

interface CommandBus {
    suspend fun <C: Command> register(handler: CommandHandler<C, *>)
    suspend fun <R> handle(command: Command): R
}