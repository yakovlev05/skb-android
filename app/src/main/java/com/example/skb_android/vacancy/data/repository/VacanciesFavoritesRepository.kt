package com.example.skb_android.vacancy.data.repository

class VacanciesFavoritesRepository {

    private val _favoritesIds = mutableSetOf<String>()

    fun getFavoritesIds(): List<String> {
        return _favoritesIds.toList()
    }

    fun isExists(vacancyId: String) = vacancyId in _favoritesIds

    fun addId(vacancyId: String) = _favoritesIds.add(vacancyId)

    fun removeId(vacancyId: String) = _favoritesIds.remove(vacancyId)
}