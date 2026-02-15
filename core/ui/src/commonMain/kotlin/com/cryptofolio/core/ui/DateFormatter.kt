package com.cryptofolio.core.ui

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object DateFormatter {
    fun formatDate(instant: Instant): String {
        val local = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        val month = local.monthNumber.toString().padStart(2, '0')
        val day = local.dayOfMonth.toString().padStart(2, '0')
        return "${day}/${month}/${local.year}"
    }

    fun formatDateTime(instant: Instant): String {
        val local = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        val month = local.monthNumber.toString().padStart(2, '0')
        val day = local.dayOfMonth.toString().padStart(2, '0')
        val hour = local.hour.toString().padStart(2, '0')
        val minute = local.minute.toString().padStart(2, '0')
        return "${day}/${month}/${local.year} ${hour}:${minute}"
    }
}
