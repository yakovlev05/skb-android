package com.example.skb_android.vacancy.impl.data.mapper

import com.example.skb_android.vacancy.impl.data.entity.VacanciesDbEntity
import com.example.skb_android.vacancy.impl.data.model.FullVacancyResponse
import com.example.skb_android.vacancy.impl.data.model.ShortVacancyResponse
import com.example.skb_android.vacancy.api.domain.model.VacancyEntity
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

    fun mapToEntity(vacanciesDbEntity: VacanciesDbEntity): VacancyEntity = VacancyEntity(
        id = vacanciesDbEntity.id,
        vacancyUrl = vacanciesDbEntity.vacancyUrl,
        name = vacanciesDbEntity.name,
        salaryFrom = vacanciesDbEntity.salaryFrom,
        salaryTo = vacanciesDbEntity.salaryTo,
        salaryModeName = vacanciesDbEntity.salaryModeName,
        publishedAt = OffsetDateTime.parse(vacanciesDbEntity.publishedAt, dateFormatter),
        employerName = vacanciesDbEntity.employerName,
        employerUrl = vacanciesDbEntity.employerUrl,
        employerLogoUrl = vacanciesDbEntity.employerLogoUrl,
        areaName = vacanciesDbEntity.areaName,
        experienceName = vacanciesDbEntity.experienceName,
        description = vacanciesDbEntity.description,
        skills = vacanciesDbEntity.skills.split("|"),
        isFavorite = vacanciesDbEntity.isFavorite,
    )

    fun mapToDbEntity(vacancyEntity: VacancyEntity): VacanciesDbEntity = VacanciesDbEntity(
        id = vacancyEntity.id,
        vacancyUrl = vacancyEntity.vacancyUrl,
        name = vacancyEntity.name,
        salaryFrom = vacancyEntity.salaryFrom,
        salaryTo = vacancyEntity.salaryTo,
        salaryModeName = vacancyEntity.salaryModeName,
        publishedAt = vacancyEntity.publishedAt.format(dateFormatter),
        employerName = vacancyEntity.employerName,
        employerUrl = vacancyEntity.employerUrl,
        employerLogoUrl = vacancyEntity.employerLogoUrl,
        areaName = vacancyEntity.areaName,
        experienceName = vacancyEntity.experienceName,
        description = vacancyEntity.description,
        skills = vacancyEntity.skills.joinToString("|"),
        isFavorite = vacancyEntity.isFavorite,
    )
}
