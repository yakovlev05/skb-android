package com.example.skb_android.vacancy.impl.presentation.mapper

import com.example.skb_android.vacancy.api.domain.model.VacancyEntity
import com.example.skb_android.vacancy.impl.presentation.model.ShortVacancyUiModel
import com.example.skb_android.vacancy.impl.presentation.viewModel.toPrettySalary

class VacanciesPresentationMapper {

    fun mapToShortUi(vacancies: List<VacancyEntity>): List<ShortVacancyUiModel> =
        vacancies.map { vacancy ->
            ShortVacancyUiModel(
                id = vacancy.id,
                vacancyUrl = vacancy.vacancyUrl,
                name = vacancy.name,
                prettySalary = toPrettySalary(
                    vacancy.salaryFrom,
                    vacancy.salaryTo,
                    vacancy.salaryModeName
                ),
                publishedAt = vacancy.publishedAt,
                employerName = vacancy.employerName,
                employerUrl = vacancy.employerUrl,
                employerLogoUrl = vacancy.employerLogoUrl,
                areaName = vacancy.areaName,
                isFavorite = vacancy.isFavorite
            )
        }

}