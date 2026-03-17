package com.example.skb_android.vacancy.data.mapper

import com.example.skb_android.vacancy.data.model.FullVacancyResponse
import com.example.skb_android.vacancy.data.model.ShortVacancyResponse
import com.example.skb_android.vacancy.domain.model.VacancyEntity
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class VacanciesMapper {

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ")

    fun mapToEntity(response: ShortVacancyResponse): VacancyEntity = VacancyEntity(
        id = response.id,
        vacancyUrl = response.alternateUrl,
        name = response.name,
        salaryFrom = response.salaryRange?.from,
        salaryTo = response.salaryRange?.to,
        salaryModeName = response.salaryRange?.mode?.name,
        publishedAt = OffsetDateTime.parse(response.publishedAt, dateFormatter),
        employerName = response.employer.name,
        employerUrl = response.employer.alternateUrl,
        employerLogoUrl = response.employer.logoUrls?.middleSize,
        areaName = response.area.name,
        experienceName = null,
        description = "",
        skills = emptyList(),
        isFavorite = false
    )

    fun mapToEntity(response: FullVacancyResponse): VacancyEntity = VacancyEntity(
        id = response.id,
        vacancyUrl = response.alternateUrl,
        name = response.name,
        salaryFrom = response.salaryRange?.from,
        salaryTo = response.salaryRange?.to,
        salaryModeName = response.salaryRange?.mode?.name,
        publishedAt = OffsetDateTime.parse(response.publishedAt, dateFormatter),
        employerName = response.employer.name,
        employerUrl = response.employer.alternateUrl,
        employerLogoUrl = response.employer.logoUrls?.middleSize,
        areaName = response.area.name,
        experienceName = response.experience?.name,
        description = response.description,
        skills = response.skills.map { it.name },
        isFavorite = false
    )
}
