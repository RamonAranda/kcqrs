package com.raranda.kcqrs.shared

import org.slf4j.LoggerFactory

internal fun <R : Any> R.logger() = LoggerFactory.getLogger(this::class.simpleName)