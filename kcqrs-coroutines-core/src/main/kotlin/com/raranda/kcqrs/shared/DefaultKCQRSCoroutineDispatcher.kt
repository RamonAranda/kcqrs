package com.raranda.kcqrs.shared

import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors

val DefaultCommandCoroutineDispatcher = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
val DefaultQueryCoroutineDispatcher = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
val DefaultEventCoroutineDispatcher = Executors.newFixedThreadPool(10).asCoroutineDispatcher()