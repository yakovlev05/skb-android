package com.example.skb_android.vacancy.domain.interactor

import com.example.skb_android.vacancy.data.repository.VacanciesFavoritesRepository
import com.example.skb_android.vacancy.data.repository.VacanciesRepository
import com.example.skb_android.vacancy.domain.model.VacancyEntity

class VacancyInteractor(
    private val vacanciesFavoritesRepository: VacanciesFavoritesRepository,
    private val vacanciesRepository: VacanciesRepository
) {
    suspend fun getVacancies(): List<VacancyEntity> {
        val favoriteIds = vacanciesFavoritesRepository.getFavoritesIds().toHashSet()
        return vacanciesRepository.getVacancies()
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
}