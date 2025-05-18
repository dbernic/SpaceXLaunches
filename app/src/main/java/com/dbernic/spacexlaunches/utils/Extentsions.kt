package com.dbernic.spacexlaunches.utils

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun String.formatDateToDDMMYYYY(): String {
    val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    isoFormat.timeZone = TimeZone.getTimeZone("UTC") // указано Z, значит UTC

    try {
        val date = isoFormat.parse(this)

        val outputFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return outputFormat.format(date!!)
    } catch (e: Exception) {
        return ""
    }

}