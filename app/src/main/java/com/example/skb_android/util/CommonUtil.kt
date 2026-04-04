package com.example.skb_android.util

import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter


private var formatterReadableDate: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

fun toReadableDate(date: OffsetDateTime): String {
    return date.format(formatterReadableDate);
}

fun MutablePreferences.setOrRemove(key: Preferences.Key<String>, value: String?) {
    if (value != null) this[key] = value else remove(key)
}