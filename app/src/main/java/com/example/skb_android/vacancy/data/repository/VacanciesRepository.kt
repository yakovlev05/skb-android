package com.example.skb_android.vacancy.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.skb_android.vacancy.data.api.VacanciesApi
import com.example.skb_android.vacancy.data.mapper.VacanciesFilterMapper
import com.example.skb_android.vacancy.data.mapper.VacanciesMapper
import com.example.skb_android.vacancy.domain.model.Experience
import com.example.skb_android.vacancy.domain.model.VacancyEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.text.set

class VacanciesRepository(
    private val vacanciesApi: VacanciesApi,
    private val vacanciesMapper: VacanciesMapper,
    private val vacanciesFilterMapper: VacanciesFilterMapper,
    private val dataStore: DataStore<Preferences>,
) {

    private val searchQueryKey = stringPreferencesKey(SEARCH_QUERY_KEY)
    private val searchExperienceFilterKey = stringPreferencesKey(SEARCH_EXPERIENCE_FILTER_KEY)

    suspend fun getVacancies(textSearch: String, experience: Experience?): List<VacancyEntity> =
        withContext(Dispatchers.IO) {
            val response = vacanciesApi.getVacancies(
                text = textSearch,
                experience = vacanciesFilterMapper.mapToApi(experience)
            )
            response.items
                .map { vacanciesMapper.mapToEntity(it) }
                .toList()
        }

    suspend fun getVacancy(vacancyId: String): VacancyEntity = withContext(Dispatchers.IO) {
        val response = vacanciesApi.getVacancy(vacancyId)
        vacanciesMapper.mapToEntity(response)
    }

    suspend fun setSearchQuery(text: String) {
        dataStore.edit { it[searchQueryKey] = text }
    }

    fun observeSearchQuery(): Flow<String> = dataStore.data
        .map { it[searchQueryKey] ?: "" }
        .distinctUntilChanged()

    suspend fun setExperienceFilter(experience: String?) {
        dataStore.edit { prefs ->
            if (experience != null) {
                prefs[searchExperienceFilterKey] = experience
            } else {
                prefs.remove(searchExperienceFilterKey)
            }
        }
    }

    fun observeExperienceFilter(): Flow<String?> = dataStore.data
        .map { it[searchExperienceFilterKey] }
        .distinctUntilChanged()

    companion object {
        private const val SEARCH_QUERY_KEY = "FILTER_SEARCH_QUERY"
        private const val SEARCH_EXPERIENCE_FILTER_KEY = "FILTER_SEARCH_EXPERIENCE"
    }
}