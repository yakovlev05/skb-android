package com.example.skb_android.vacancy.data.api

import com.example.skb_android.vacancy.data.model.FullVacancyResponse
import com.example.skb_android.vacancy.data.model.VacanciesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface VacanciesApi {

    @GET("/vacancies")
    suspend fun getVacancies(
        @Query("sort_by") sortBy: String = "relevance",
        @Query("page") page: Int = 0,
        @Query("per_page") perPage: Int = 10
    ): VacanciesResponse

    @GET("/vacancies/{vacancyId}")
    suspend fun getVacancy(@Path("vacancyId") vacancyId: String): FullVacancyResponse
}