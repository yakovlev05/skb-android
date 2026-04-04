package com.example.skb_android.vacancy.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.skb_android.vacancy.data.entity.VacanciesDbEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VacanciesDao {

    @Query("SELECT * FROM VacanciesDbEntity")
    suspend fun getAll(): List<VacanciesDbEntity>

    @Query("SELECT * FROM VacanciesDbEntity")
    fun observeAll(): Flow<List<VacanciesDbEntity>>

    @Query("SELECT id FROM VacanciesDbEntity")
    suspend fun getAllIds(): List<String>

    @Query("SELECT id FROM VacanciesDbEntity")
    fun observeAllIds(): Flow<List<String>>

    @Query("SELECT EXISTS(SELECT 1 FROM VacanciesDbEntity WHERE id = :id)")
    suspend fun isExists(id: String): Boolean

    @Insert
    suspend fun insert(vacanciesDbEntity: VacanciesDbEntity)

    @Query("DELETE FROM VacanciesDbEntity WHERE id = :id")
    suspend fun deleteById(id: String)
}