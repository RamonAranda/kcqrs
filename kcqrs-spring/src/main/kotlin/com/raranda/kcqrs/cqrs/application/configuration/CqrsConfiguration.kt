package com.raranda.kcqrs.cqrs.application.configuration

import com.raranda.kcqrs.command.domain.CommandBus
import com.raranda.kcqrs.command.domain.CommandHandler
import com.raranda.kcqrs.command.infrastructure.SimpleCommandBus
import com.raranda.kcqrs.event.domain.EventBus
import com.raranda.kcqrs.event.domain.EventHandler
import com.raranda.kcqrs.event.infrastructure.bus.SyncEventBus
import com.raranda.kcqrs.query.domain.QueryBus
import com.raranda.kcqrs.query.domain.QueryHandler
import com.raranda.kcqrs.query.infrastructure.SimpleQueryBus
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class CqrsConfiguration {

    @Bean
    @ConditionalOnMissingBean
    open fun commandBus(handlers: List<CommandHandler<*, *>>): CommandBus =
            SimpleCommandBus().apply { handlers.forEach { register(it) } }

    @Bean
    @ConditionalOnMissingBean
    open fun queryBus(handlers: List<QueryHandler<*, *>>): QueryBus =
            SimpleQueryBus().apply { handlers.forEach { register(it) } }

    @Bean
    @ConditionalOnMissingBean
    open fun eventBus(handlers: List<EventHandler<*>>): EventBus =
            SyncEventBus().apply { handlers.forEach { register(it) } }
    
}