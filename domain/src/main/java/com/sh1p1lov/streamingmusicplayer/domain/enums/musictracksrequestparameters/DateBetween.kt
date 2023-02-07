package com.sh1p1lov.streamingmusicplayer.domain.enums.musictracksrequestparameters

import java.text.SimpleDateFormat
import java.util.*

class DateBetween() {
    var value = ""
        private set

    companion object {
        private const val DATE_FORMAT = "yyyy-MM-dd"
    }

    @Suppress("SimpleDateFormat")
    constructor(dateFrom: Date, dateTo: Date) : this() {
        val dateFormat = SimpleDateFormat(DATE_FORMAT)
        value = "${dateFormat.format(dateFrom)}_${dateFormat.format(dateTo)}"
    }

    @Suppress("SimpleDateFormat")
    fun set(dateFrom: Date, dateTo: Date) {
        val dateFormat = SimpleDateFormat(DATE_FORMAT)
        value = "${dateFormat.format(dateFrom)}_${dateFormat.format(dateTo)}"
    }

    fun setDefault() {
        value = ""
    }
}