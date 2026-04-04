package com.example.skb_android.vacancy.data.repository

import com.example.skb_android.vacancy.data.dao.VacanciesDao
import com.example.skb_android.vacancy.data.mapper.VacanciesMapper
import com.example.skb_android.vacancy.domain.model.VacancyEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class VacanciesFavoritesRepository(
    private val vacanciesDao: VacanciesDao,
    private val vacanciesMapper: VacanciesMapper
) {
    suspend fun getFavoritesIds(): List<String> = withContext(Dispatchers.IO) {
        vacanciesDao.getAllIds()
    }

    suspend fun getAllFavorites(): List<VacancyEntity> = withContext(Dispatchers.IO) {
        vacanciesDao.getAll().map { vacanciesMapper.mapToEntity(it) }
    }

    fun observeAllFavorites(): Flow<List<VacancyEntity>> = vacanciesDao
        .observeAll()
        .map { list -> list.map { vacanciesMapper.mapToEntity(it) } }
        .flowOn(Dispatchers.IO)

    fun observeFavoriteIds(): Flow<Set<String>> = vacanciesDao
        .observeAllIds()
        .map { it.toHashSet() }

    suspend fun isExists(vacancyId: String) =
        withContext(Dispatchers.IO) {
            vacanciesDao.isExists(vacancyId)
        }

    suspend fun save(vacancyEntity: VacancyEntity) =
        withContext(Dispatchers.IO) {
            vacanciesDao.insert(
                vacanciesMapper.mapToDbEntity(vacancyEntity)
            )
        }

    suspend fun removeId(vacancyId: String) =
        withContext(Dispatchers.IO) {
            vacanciesDao.deleteById(vacancyId)
        }
}