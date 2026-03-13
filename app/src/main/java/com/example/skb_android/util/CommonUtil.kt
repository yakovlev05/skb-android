package com.example.skb_android.util

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter


private var formatterReadableDate: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

fun toReadableDate(date: OffsetDateTime): String {
    return date.format(formatterReadableDate);
}