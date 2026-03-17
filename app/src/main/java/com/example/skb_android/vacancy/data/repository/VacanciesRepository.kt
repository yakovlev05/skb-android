package com.example.skb_android.vacancy.data.repository

import com.example.skb_android.vacancy.data.api.VacanciesApi
import com.example.skb_android.vacancy.data.mapper.VacanciesMapper
import com.example.skb_android.vacancy.domain.model.VacancyEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VacanciesRepository(
    private val vacanciesApi: VacanciesApi,
    private val vacanciesMapper: VacanciesMapper
) {

    suspend fun getVacancies(): List<VacancyEntity> = withContext(Dispatchers.IO) {
        val response = vacanciesApi.getVacancies()
        response.items
            .map { vacanciesMapper.mapToEntity(it) }
            .toList()
    }

    suspend fun getVacancy(vacancyId: String): VacancyEntity = withContext(Dispatchers.IO) {
        val response = vacanciesApi.getVacancy(vacancyId)
        vacanciesMapper.mapToEntity(response)
    }

}