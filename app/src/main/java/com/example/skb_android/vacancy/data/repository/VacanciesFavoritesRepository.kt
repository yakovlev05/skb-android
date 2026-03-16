package com.example.skb_android.vacancy.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class VacanciesFavoritesRepository {
    private val _favoritesIds = MutableStateFlow<Set<String>>(emptySet())
    val favoritesIds: StateFlow<Set<String>> = _favoritesIds

    fun toggle(vacancyId: String) {
        _favoritesIds.update {
            if (vacancyId in it) {
                it - vacancyId
            } else {
                it + vacancyId
            }
        }
    }

}