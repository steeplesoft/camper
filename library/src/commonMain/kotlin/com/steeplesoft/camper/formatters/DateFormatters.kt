package com.steeplesoft.camper.formatters

import kotlinx.datetime.LocalDate

fun dateShort(r: LocalDate?): String {
    return if (r != null) LocalDate.Formats.ISO.format(r) else ""
}

fun dateLong(r: LocalDate?): String {
    return if (r != null) LocalDate.Formats.ISO.format(r) else ""
}
