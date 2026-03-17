package com.example.skb_android.vacancy.presentation.model

import java.time.OffsetDateTime

data class VacancyFullUiModel(
    val id: String,
    val vacancyUrl: String,
    val name: String,
    val prettySalary: String?,
    val publishedAt: OffsetDateTime,
    val employerName: String,
    val employerUrl: String?,
    val employerLogoUrl: String?,
    val areaName: String,
    val experienceName: String?,
    val description: String,
    val skills: List<String>,
    val isFavorite: Boolean
)
