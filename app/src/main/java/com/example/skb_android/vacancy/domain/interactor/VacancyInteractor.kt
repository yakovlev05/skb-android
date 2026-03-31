package com.example.skb_android.vacancy.domain.interactor

import com.example.skb_android.vacancy.data.repository.VacanciesFavoritesRepository
import com.example.skb_android.vacancy.data.repository.VacanciesRepository
import com.example.skb_android.vacancy.domain.model.Experience
import com.example.skb_android.vacancy.domain.model.VacancyEntity
import kotlinx.coroutines.flow.map

class VacancyInteractor(
    private val vacanciesFavoritesRepository: VacanciesFavoritesRepository,
    private val vacanciesRepository: VacanciesRepository
) {
    suspend fun getVacancies(textSearch: String, experience: Experience?): List<VacancyEntity> {
        val favoriteIds = vacanciesFavoritesRepository.getFavoritesIds().toHashSet()
        return vacanciesRepository.getVacancies(textSearch, experience)
            .map { it.copy(isFavorite = it.id in favoriteIds) }
    }

    suspend fun getVacancy(vacancyId: String): VacancyEntity {
        return vacanciesRepository.getVacancy(vacancyId)
            .copy(isFavorite = vacanciesFavoritesRepository.isExists(vacancyId))
    }

    fun toggleFavoriteVacancy(vacancyId: String) {
        if (vacanciesFavoritesRepository.isExists(vacancyId)) {
            vacanciesFavoritesRepository.removeId(vacancyId)
        } else {
            vacanciesFavoritesRepository.addId(vacancyId)
        }
    }

    suspend fun setSearchQuery(text: String) = vacanciesRepository.setSearchQuery(text)

    fun observeSearchQuery() = vacanciesRepository.observeSearchQuery()

    fun observeExperienceFilter() = vacanciesRepository
        .observeExperienceFilter()
        .map { if (it == null) null else Experience.valueOf(it) }

    suspend fun setExperienceFilter(experience: Experience?) {
        vacanciesRepository.setExperienceFilter(experience?.name)
    }
}