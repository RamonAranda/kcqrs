package com.raranda.kcqrs.command.infrastructure

import com.raranda.kcqrs.command.domain.Command
import com.raranda.kcqrs.command.domain.CommandHandler
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.jvmErasure

internal fun <Q : Command> CommandHandler<Q, *>.commandClass() =
        javaClass.kotlin
                .supertypes.first { it.jvmErasure.isSubclassOf(CommandHandler::class) }
                .arguments.first()
                .type!!.jvmErasure