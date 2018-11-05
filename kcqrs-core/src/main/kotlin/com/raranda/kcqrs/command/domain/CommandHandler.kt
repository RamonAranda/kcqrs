package com.raranda.kcqrs.command.domain

interface CommandHandler<in C: Command, R> {
    fun on(command: C): R
}