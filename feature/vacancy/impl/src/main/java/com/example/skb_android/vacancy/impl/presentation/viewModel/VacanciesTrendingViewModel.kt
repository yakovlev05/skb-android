package com.example.skb_android.vacancy.impl.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skb_android.core.util.launchCatching
import com.example.skb_android.navigation.MyBackStack
import com.example.skb_android.navigation.VacancyFullRoute
import com.example.skb_android.vacancy.api.domain.interactor.VacancyInteractor
import com.example.skb_android.vacancy.api.domain.model.Experience
import com.example.skb_android.vacancy.impl.presentation.cache.FilterBadgeCache
import com.example.skb_android.vacancy.impl.presentation.mapper.VacanciesPresentationMapper
import com.example.skb_android.vacancy.impl.presentation.model.ShortVacancyUiModel
import com.example.skb_android.vacancy.impl.presentation.model.VacanciesSearchQueryState
import com.example.skb_android.vacancy.impl.presentation.model.VacanciesTrendingState
import com.example.skb_android.vacancy.impl.presentation.model.VacancyExperience
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VacanciesTrendingViewModel(
    private val vacancyInteractor: VacancyInteractor,
    private val myBackStack: MyBackStack,
    private val vacanciesPresentationMapper: VacanciesPresentationMapper,
    private val filterBadgeCache: FilterBadgeCache,
) : ViewModel() {
    val hasActiveFilters = filterBadgeCache.hasActiveFilters

    private val _mutableVacanciesState = MutableStateFlow(VacanciesTrendingState())
    private val _mutableSearchState = MutableStateFlow(VacanciesSearchQueryState())

    val vacanciesState = _mutableVacanciesState.asStateFlow()
    val searchState = _mutableSearchState.asStateFlow()

    init {
        initFilterFlow()
        observeFavoriteIds()
    }

    fun onFavoriteClick(vacancy: ShortVacancyUiModel) {
        launchCatching(
            onError = { Log.e(TAG, "Failed to toggle favorite: ${it.message}") }
        ) {
            vacancyInteractor.toggleFavoriteVacancy(vacancy.id)
        }
    }

    fun onVacancyClick(vacancy: ShortVacancyUiModel) {
        myBackStack.add(VacancyFullRoute(vacancy.id))
    }

    fun onSearchQueryInput(text: String) {
        _mutableSearchState.update { it.copy(text = text) }
    }

    fun onSelectExperience(vacancyExperience: VacancyExperience) {
        var newExperience: VacancyExperience? = null
        _mutableSearchState.update {
            newExperience = if (it.experience == vacancyExperience) null else vacancyExperience
            it.copy(experience = newExperience)
        }

        launchCatching {
            vacancyInteractor.setExperienceFilter(mapToEntity(newExperience))
        }

    }

    fun onSearchClick() {
        viewModelScope.launch {
            vacancyInteractor.setSearchQuery(searchState.value.text)
        }
    }

    private fun initFilterFlow() {
        launchCatching {
            combine(
                vacancyInteractor.observeSearchQuery(),
                vacancyInteractor.observeExperienceFilter()
            ) { text, experience ->
                val isDefault = text == "" && experience == null
                filterBadgeCache.update(isDefault)
                _mutableSearchState.update {
                    it.copy(
                        text = text,
                        experience = mapToUi(experience),
                    )
                }
            }
                .collect { loadVacancies() }
        }
    }

    private fun loadVacancies() {
        launchCatching(
            onError = {
                Log.e(TAG, "Failed to load vacancies: ${it.message}")
                updateVacanciesState(VacanciesTrendingState.State.Error(it.message.orEmpty()))
            }
        ) {
            updateVacanciesState(VacanciesTrendingState.State.Loading)
            val vacancies = vacancyInteractor.getVacancies(
                textSearch = _mutableSearchState.value.text,
                experience = mapToEntity(_mutableSearchState.value.experience)
            )
            updateVacanciesState(
                VacanciesTrendingState.State.Success(
                    vacanciesPresentationMapper.mapToShortUi(
                        vacancies
                    )
                )
            )
        }
    }

    private fun observeFavoriteIds() {
        launchCatching(
            onError = { Log.e(TAG, "Failed update favorite vacancies ids: ${it.message}") }
        ) {
            vacancyInteractor.observeFavoriteIds()
                .collect { ids ->
                    _mutableVacanciesState.update { current ->
                        val success =
                            current.state as? VacanciesTrendingState.State.Success ?: return@collect
                        val updatedVacancies =
                            success.vacancies.map { it.copy(isFavorite = it.id in ids) }
                        current.copy(state = success.copy(vacancies = updatedVacancies))
                    }
                }
        }
    }

    private fun updateVacanciesState(state: VacanciesTrendingState.State) {
        _mutableVacanciesState.update { it.copy(state = state) }
    }

    private fun mapToEntity(experience: VacancyExperience?): Experience? =
        when (experience) {
            VacancyExperience.NO_EXPERIENCE -> Experience.NO_EXPERIENCE
            VacancyExperience.BETWEEN_1_AND_3 -> Experience.BETWEEN_1_AND_3
            VacancyExperience.BETWEEN_3_AND_6 -> Experience.BETWEEN_3_AND_6
            VacancyExperience.MORE_THAN_6 -> Experience.MORE_THAN_6
            null -> null
        }

    private fun mapToUi(experience: Experience?): VacancyExperience? =
        when (experience) {
            Experience.NO_EXPERIENCE -> VacancyExperience.NO_EXPERIENCE
            Experience.BETWEEN_1_AND_3 -> VacancyExperience.BETWEEN_1_AND_3
            Experience.BETWEEN_3_AND_6 -> VacancyExperience.BETWEEN_3_AND_6
            Experience.MORE_THAN_6 -> VacancyExperience.MORE_THAN_6
            null -> null
        }

    companion object {
        private val TAG = VacanciesTrendingViewModel::class.simpleName
    }
}
