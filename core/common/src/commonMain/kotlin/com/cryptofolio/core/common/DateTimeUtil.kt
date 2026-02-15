package com.cryptofolio.core.common

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object DateTimeUtil {
    fun now(): Instant = Clock.System.now()

    fun Instant.toLocalDateTime(): LocalDateTime =
        this.toLocalDateTime(TimeZone.currentSystemDefault())

    fun Long.toInstant(): Instant = Instant.fromEpochMilliseconds(this)

    fun Instant.toEpochMillis(): Long = this.toEpochMilliseconds()
}
