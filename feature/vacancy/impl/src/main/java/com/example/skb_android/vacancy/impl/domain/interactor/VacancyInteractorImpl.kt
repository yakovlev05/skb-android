package com.example.skb_android.vacancy.impl.domain.interactor

import com.example.skb_android.vacancy.api.domain.interactor.VacancyInteractor
import com.example.skb_android.vacancy.impl.data.repository.VacanciesFavoritesRepository
import com.example.skb_android.vacancy.impl.data.repository.VacanciesRepository
import com.example.skb_android.vacancy.api.domain.model.Experience
import com.example.skb_android.vacancy.api.domain.model.VacancyEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class VacancyInteractorImpl(
    private val vacanciesFavoritesRepository: VacanciesFavoritesRepository,
    private val vacanciesRepository: VacanciesRepository
) : VacancyInteractor {
    override suspend fun getVacancies(textSearch: String, experience: Experience?): List<VacancyEntity> {
        val favoriteIds = vacanciesFavoritesRepository.getFavoritesIds().toHashSet()
        return vacanciesRepository.getVacancies(textSearch, experience)
            .map { it.copy(isFavorite = it.id in favoriteIds) }
    }

    override suspend fun getVacancy(vacancyId: String): VacancyEntity {
        return vacanciesRepository.getVacancy(vacancyId)
            .copy(isFavorite = vacanciesFavoritesRepository.isExists(vacancyId))
    }

    override suspend fun toggleFavoriteVacancy(vacancyId: String) {
        if (vacanciesFavoritesRepository.isExists(vacancyId)) {
            vacanciesFavoritesRepository.removeId(vacancyId)
        } else {
            val fullVacancy = getVacancy(vacancyId)
            vacanciesFavoritesRepository.save(fullVacancy)
        }
    }

    override suspend fun setSearchQuery(text: String) = vacanciesRepository.setSearchQuery(text)

    override fun observeSearchQuery() = vacanciesRepository.observeSearchQuery()

    override fun observeExperienceFilter() = vacanciesRepository
        .observeExperienceFilter()
        .map { if (it == null) null else Experience.valueOf(it) }

    override suspend fun setExperienceFilter(experience: Experience?) {
        this.vacanciesRepository.setExperienceFilter(experience?.name)
    }

    override suspend fun getAllFavorites() = vacanciesFavoritesRepository.getAllFavorites()

    override fun observeAllFavorites(): Flow<List<VacancyEntity>> =
        vacanciesFavoritesRepository.observeAllFavorites()

    override fun observeFavoriteIds(): Flow<Set<String>> =
        vacanciesFavoritesRepository.observeFavoriteIds()

    override suspend fun deleteFavorite(vacancyId: String) = vacanciesFavoritesRepository.removeId(vacancyId)
}