package com.example.skb_android.vacancy.api.domain.interactor

import com.example.skb_android.vacancy.api.domain.model.Experience
import com.example.skb_android.vacancy.api.domain.model.VacancyEntity
import kotlinx.coroutines.flow.Flow

interface VacancyInteractor {
    suspend fun getVacancies(textSearch: String, experience: Experience?): List<VacancyEntity>

    suspend fun getVacancy(vacancyId: String): VacancyEntity

    suspend fun toggleFavoriteVacancy(vacancyId: String)

    suspend fun setSearchQuery(text: String)

    fun observeSearchQuery(): Flow<String>

    fun observeExperienceFilter(): Flow<Experience?>

    suspend fun setExperienceFilter(experience: Experience?)

    suspend fun getAllFavorites(): List<VacancyEntity>

    fun observeAllFavorites(): Flow<List<VacancyEntity>>

    fun observeFavoriteIds(): Flow<Set<String>>

    suspend fun deleteFavorite(vacancyId: String)
}