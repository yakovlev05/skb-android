package com.example.skb_android.vacancy.api.domain.model

import java.time.OffsetDateTime

data class VacancyEntity(
    val id: String,
    val vacancyUrl: String,
    val name: String,
    val salaryFrom: Int?,
    val salaryTo: Int?,
    val salaryModeName: String?,
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