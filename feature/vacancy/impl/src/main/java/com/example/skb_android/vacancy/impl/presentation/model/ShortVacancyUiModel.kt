package com.example.skb_android.vacancy.impl.presentation.model

import java.time.OffsetDateTime

data class ShortVacancyUiModel(
    val id: String,
    val vacancyUrl: String,
    val name: String,
    val prettySalary: String?,
    val publishedAt: OffsetDateTime,
    val employerName: String,
    val employerUrl: String?,
    val employerLogoUrl: String?,
    val areaName: String,
    val isFavorite: Boolean
)
