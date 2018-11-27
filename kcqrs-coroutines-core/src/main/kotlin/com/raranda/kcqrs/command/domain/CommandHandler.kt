package com.raranda.kcqrs.command.domain

interface CommandHandler<in C : Command, R> {
    suspend fun on(command: C): R
}