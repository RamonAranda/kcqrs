package com.raranda.kcqrs.shared

import org.slf4j.LoggerFactory
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.jvmErasure

fun <R : Any> R.logger() = LoggerFactory.getLogger(this::class.simpleName)

inline fun <reified T : Any> T.firstSuperClassGeneric() =
        javaClass.kotlin
                .supertypes.firstOrNull { !it.jvmErasure.isSubclassOf(KClass::class) }
                ?.arguments?.first()
                ?.type?.jvmErasure