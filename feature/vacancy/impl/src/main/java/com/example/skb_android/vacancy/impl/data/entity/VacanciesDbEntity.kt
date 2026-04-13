package com.example.skb_android.vacancy.impl.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class VacanciesDbEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "vacancy_url") val vacancyUrl: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "salary_from") val salaryFrom: Int?,
    @ColumnInfo(name = "salary_to") val salaryTo: Int?,
    @ColumnInfo(name = "salary_mode_name") val salaryModeName: String?,
    @ColumnInfo(name = "published_at") val publishedAt: String,
    @ColumnInfo(name = "employer_name") val employerName: String,
    @ColumnInfo(name = "employer_url") val employerUrl: String?,
    @ColumnInfo(name = "employer_logo_url") val employerLogoUrl: String?,
    @ColumnInfo(name = "area_name") val areaName: String,
    @ColumnInfo(name = "experience_name") val experienceName: String?,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "skills") val skills: String,
    @ColumnInfo(name = "is_favorite") val isFavorite: Boolean,
)
