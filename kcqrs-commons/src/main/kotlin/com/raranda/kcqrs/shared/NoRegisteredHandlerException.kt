package com.raranda.kcqrs.shared

import com.raranda.kcqrs.command.domain.Command
import com.raranda.kcqrs.query.domain.Query

class NoRegisteredHandlerException private constructor(message: String): RuntimeException(message) {
    companion object {
        private const val MESSAGE = "No handler was registered for <%s>."
        fun forQuery(query: Query) =
                NoRegisteredHandlerException(MESSAGE.format(query.javaClass.kotlin.simpleName))
        fun forCommand(command: Command) =
                NoRegisteredHandlerException(MESSAGE.format(command.javaClass.kotlin.simpleName))
    }
}